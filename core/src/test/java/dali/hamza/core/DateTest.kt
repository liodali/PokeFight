package dali.hamza.core

import dali.hamza.core.utilities.DateManager
import org.junit.Assert
import org.junit.Test
import java.util.*

class DateTest {
    @Test
    fun testDiffDATE() {

        val calendar = Calendar.getInstance()
        calendar.time = DateManager.now()
        val min = calendar.get(Calendar.MINUTE)
        calendar.set(Calendar.MINUTE, min - 1)
        val date = calendar.time
        val diffMessage = DateManager.difference2Date(date)

        assert(diffMessage.minutes == 1)


    }
}