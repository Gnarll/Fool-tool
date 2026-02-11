package com.example.fool_tool.core.extensions

import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime
import kotlin.test.assertEquals

class DateOperationsTest {
    @Test
    fun `LocalDateTime properly converts to millis and back considering time zone`() {
        val initialDate = LocalDateTime.now()
        val initialDateInMillis = initialDate.toMillisWithZone()
        val recoveredDate = initialDateInMillis.toLocalDateTimeWithZone()
        val recoveredDateInMillis = recoveredDate.toMillisWithZone()

        assertEquals(initialDateInMillis, recoveredDateInMillis)
    }


    @Test
    fun `LocalDate properly converts to millis and back considering time zone`() {
        val initialDate = LocalDate.now()
        val initialDateInMillis = initialDate.toMillisWithZone()
        val recoveredDate = initialDateInMillis.toLocalDateWithZone()
        val recoveredDateInMillis = recoveredDate.toMillisWithZone()

        assertEquals(initialDateInMillis, recoveredDateInMillis)
    }
 
}