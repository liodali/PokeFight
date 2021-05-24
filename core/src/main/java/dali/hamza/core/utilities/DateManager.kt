package dali.hamza.core.utilities

import java.text.SimpleDateFormat
import java.util.*

object DateManager {

    private var locale = Locale.getDefault()
    val dateFormat_full = SimpleDateFormat("dd MM yyyy HH:mm", locale)
    fun setDate(year: Int, month: Int, day: Int): Date {

        val calendar = Calendar.getInstance()

        calendar[Calendar.DAY_OF_MONTH] = day

        calendar[Calendar.MONTH] = month

        calendar[Calendar.YEAR] = year
        return calendar.time
    }

    fun now(): Date {
        val calendar = Calendar.getInstance()
        return calendar.time
    }

    fun difference2Date(d1: Date, d2: Date = now()): String {
        val diff: Long = d2.time - d1.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24


        return when {
            days > 1 -> {
                return  dateFormat_full.format(Date(diff))
            }
            else -> {
                if (hours in 1..23) {
                    return "${hours.toInt()}h ago"
                }
                if (minutes in 0..59) {
                    return "${minutes.toInt()}min ago"
                }
                if (seconds in 2..59) {
                    return "${seconds.toInt()}second ago"
                }
                "now"
            }
        }
    }

}