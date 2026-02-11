package com.example.fool_tool.ui.presentation.viewmodel.reminder

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.fool_tool.core_android.alarm.AlarmScheduler
import com.example.fool_tool.core_android.notification.NotificationService
import com.example.fool_tool.domain.model.Reminder
import com.example.fool_tool.domain.model.ScheduleResult
import com.example.fool_tool.domain.repository.ReminderRepository
import com.example.fool_tool.domain.use_case.ActivateReminderUseCase
import com.example.fool_tool.domain.use_case.CancelReminderUseCase
import com.example.fool_tool.domain.use_case.DeleteReminderUseCase
import com.example.fool_tool.ui.presentation.ui_state.PermissionsUiState
import com.example.fool_tool.ui.presentation.ui_state.reminder.RemindersUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ReminderViewModel.Factory::class)
class ReminderViewModel @AssistedInject constructor(
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler,
    private val deleteReminderUseCase: DeleteReminderUseCase,
    private val cancelReminderUseCase: CancelReminderUseCase,
    private val activateReminderUseCase: ActivateReminderUseCase,
    private val notificationService: NotificationService,
    private val savedStateHandle: SavedStateHandle,
    @Assisted private val reminderIdFromNotification: Long?
) :
    ViewModel() {


    private val _reminders: MutableStateFlow<RemindersUiState> = MutableStateFlow(
        RemindersUiState(
            pagingRemindersFlow = reminderRepository.getPagedReminders().cachedIn(viewModelScope),
            isPreloading = true,
            indexToScrollTo = null,
        )
    )
    val reminders: StateFlow<RemindersUiState> = _reminders.asStateFlow()

    init {
        viewModelScope.launch {
            var reminderOffset: Int? = null

            reminderIdFromNotification?.let {
                reminderOffset = reminderRepository.getReminderOffset(reminderIdFromNotification)
            }

            _reminders.update { reminders ->
                reminders.copy(
                    isPreloading = false,
                    indexToScrollTo = reminderOffset
                )
            }
        }
    }

    private val _permissionsState: MutableStateFlow<PermissionsUiState> = MutableStateFlow(
        PermissionsUiState(
            notification = checkIsPrimaryPermissionGranted(),
            notificationChannel = checkIsReminderChannelPermissionGranted(),
            alarm = checkIsAlarmPermissionGranted()
        )
    )

    val permissionsState = _permissionsState.asStateFlow()


    var reminderToEdit: MutableState<Long?> =
        mutableStateOf(savedStateHandle[REMINDER_ID_TO_EDIT_KEY])
        private set


    fun unselectReminderToEdit() {
        reminderToEdit.value = null
    }

    fun selectReminderToEdit(reminderId: Long) {
        savedStateHandle[REMINDER_ID_TO_EDIT_KEY] = reminderId
        reminderToEdit.value = reminderId
    }

    suspend fun deleteReminder(id: Long) {
        deleteReminderUseCase(id)
    }

    suspend fun onCancelReminder(reminder: Reminder) {
        cancelReminderUseCase(reminder)
    }

    suspend fun activateReminder(reminder: Reminder): ScheduleResult =
        activateReminderUseCase(reminder)


    fun checkAndUpdatePermissions() {
        val notificationPermission = checkIsPrimaryPermissionGranted()
        val notificationChannelPermission =
            checkIsReminderChannelPermissionGranted()
        val alarmPermission = checkIsAlarmPermissionGranted()

        _permissionsState.value = with(_permissionsState.value) {
            copy(
                notification = notificationPermission,
                notificationChannel = notificationChannelPermission,
                alarm = alarmPermission,
            )
        }
    }

    fun grantNotificationPermission() {
        notificationService.openPrimaryPermissionSettings()
    }

    fun grantNotificationChannelPermission() {
        notificationService.openReminderChannelPermissionSettings()
    }

    fun grantAlarmPermission() {
        alarmScheduler.openAlarmPermissionSettings()
    }

    private fun checkIsPrimaryPermissionGranted() =
        notificationService.checkIsPrimaryPermissionGranted()

    private fun checkIsReminderChannelPermissionGranted() =
        notificationService.checkIsReminderChannelPermissionGranted()

    private fun checkIsAlarmPermissionGranted() = alarmScheduler.checkIsAlarmPermissionGranted()

    companion object {
        const val REMINDER_ID_TO_EDIT_KEY = "SAVED_STATE_REMINDER_TO_EDIT_KEY"
    }

    @AssistedFactory
    interface Factory {
        fun create(reminderIdFromNotification: Long?): ReminderViewModel
    }

}

