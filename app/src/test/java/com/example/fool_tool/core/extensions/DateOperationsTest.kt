package com.example.fool_tool.core.extensions

import org.junit.Test
import kotlin.test.assertEquals

class DateOperationsTest {
    @Test
    fun `LocalDateTime properly converts to millis and back considering time zone`() {
        val currentTime = System.currentTimeMillis()
        val localDateTime = currentTime.toLocalDateTimeWithZone()
        val recoveredTime = localDateTime.toMillisWithZone()

        assertEquals(currentTime, recoveredTime)
    }


    @Test
    fun `LocalDate properly converts to millis and back considering time zone`() {
        val currentTime = System.currentTimeMillis()
        val localDate = currentTime.toLocalDateWithZone()
        val recoveredTime = localDate.toMillisWithZone()

        val recoveredDate = recoveredTime.toLocalDateWithZone()

        assertEquals(localDate, recoveredDate)
    }

}