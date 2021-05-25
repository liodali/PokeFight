package dali.hamza.pokemongofight.common

import dali.hamza.core.utilities.DateExpiration
import dali.hamza.core.utilities.DateManager
import java.util.*

fun showDateDiffMessage( dateCreatedAt: String): String {
    val captured = DateManager.convertStringFromFormatApiToApp(date = dateCreatedAt)
    val dateExpiration = DateManager.difference2Date(d1=Date(captured))
    when {
        dateExpiration.days > 1 -> {
            //  return  dateFormat_full.format(Date(diff))
            return "at "+ DateManager.dateFormat_full.format(Date(captured))
        }
        else -> {
            if (dateExpiration.hours in 1..23) {
                return "${dateExpiration.hours}h ago"
                //return "${hours.toInt()}h ago"
            }
            if (dateExpiration.minutes in 0..59) {
                return "${dateExpiration.minutes}min ago"
                //return "${minutes.toInt()}min ago"
            }
            return "${dateExpiration.seconds}seconds"
        }
    }


}