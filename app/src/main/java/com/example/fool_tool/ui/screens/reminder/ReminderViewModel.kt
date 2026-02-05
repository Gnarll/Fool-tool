package com.example.fool_tool.ui.screens.reminder

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.fool_tool.data.alarm.AlarmScheduler
import com.example.fool_tool.data.alarm.ScheduleResult
import com.example.fool_tool.data.notifications.NotificationsService
import com.example.fool_tool.data.repositories.ReminderRepository
import com.example.fool_tool.data.use_cases.RemindersProcessingUseCase
import com.example.fool_tool.ui.model.Reminder
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = ReminderViewModel.Factory::class)
class ReminderViewModel @AssistedInject constructor(
    private val reminderRepository: ReminderRepository,
    private val alarmScheduler: AlarmScheduler,
    private val remindersProcessingUseCase: RemindersProcessingUseCase,
    private val notificationsService: NotificationsService,
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
                    indexToScrollTo = reminderOffset?.let { it - 1 })
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
        remindersProcessingUseCase.deleteReminder(id)
    }

    suspend fun onCancelReminder(reminder: Reminder) {
        remindersProcessingUseCase.cancelReminder(reminder)
    }

    suspend fun activateReminder(reminder: Reminder): ScheduleResult =
        remindersProcessingUseCase.activateReminder(reminder)


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
        notificationsService.openPrimaryPermissionSettings()
    }

    fun grantNotificationChannelPermission() {
        notificationsService.openReminderChannelPermissionSettings()
    }

    fun grantAlarmPermission() {
        alarmScheduler.openAlarmPermissionSettings()
    }

    private fun checkIsPrimaryPermissionGranted() =
        notificationsService.checkIsPrimaryPermissionGranted()

    private fun checkIsReminderChannelPermissionGranted() =
        notificationsService.checkIsReminderChannelPermissionGranted()

    private fun checkIsAlarmPermissionGranted() = alarmScheduler.checkIsAlarmPermissionGranted()

    companion object {
        const val REMINDER_ID_TO_EDIT_KEY = "SAVED_STATE_REMINDER_TO_EDIT_KEY"
    }

    @AssistedFactory
    interface Factory {
        fun create(reminderIdFromNotification: Long?): ReminderViewModel
    }

}

data class RemindersUiState(
    val isPreloading: Boolean,
    val indexToScrollTo: Int?,
    val pagingRemindersFlow: Flow<PagingData<Reminder>>
)

data class PermissionsUiState(
    val notification: Boolean,
    val notificationChannel: Boolean,
    val alarm: Boolean,
) {
    val areAllGranted: Boolean
        get() = notification && notificationChannel && alarm
}
