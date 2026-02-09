package com.example.fool_tool.domain.model

sealed interface ScheduleResult {
    object Success : ScheduleResult
    object FailedWithNoPermission : ScheduleResult
    object FailedWithInvalidTime : ScheduleResult

}