
package com.ramonpsatu.columnchart.utils

import java.util.Calendar
import java.util.TimeZone

/**
 * [MyCalendar] calendar instance.
 * @property month returns the month of the year.
 * @property year returns the number of the current year.
 */
object MyCalendar {

    private val calendarDay: Calendar = Calendar.getInstance(TimeZone.getDefault())
    val month = calendarDay.get(Calendar.MONTH)
    var year = calendarDay.get(Calendar.YEAR)


}