package com.focuzar.holoassist.utilities

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Utils {

    fun getDate(time: Long): String? {
        return try {
            val sdf = SimpleDateFormat("d MMM yyyy")
            val netDate = Date(time)
            sdf.format(netDate)
        } catch (ex: Exception) {
            "xx"
        }
    }

    fun remainingDays(endDate: String): String? {
        var days: Long = 0
        println("dateeee$endDate")
        val sdf = SimpleDateFormat("d MMM yyyy")
        try {
            val dateEnd = sdf.parse(endDate)
            val now = Date(System.currentTimeMillis())
            days = getDateDiff(
                    dateEnd,
                    now,
                    TimeUnit.DAYS
            )
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return days.toString() + ""
    }

    private fun getDateDiff(
            date1: Date,
            date2: Date,
            timeUnit: TimeUnit
    ): Long {
        val diffInMillies = date1.time - date2.time
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
    }

    fun returnDate(timestamp: Long): String {
        var displayDate = ""
        val dateLocal: String = Utils().getDate(timestamp)!!
        val c = Calendar.getInstance().timeInMillis
        val df = SimpleDateFormat("d MMM yyyy")
        val formattedDate = df.format(c)
        if (formattedDate.equals(dateLocal, ignoreCase = true)) {
            displayDate = "Today"
        } else if (Utils().remainingDays(dateLocal) == "-1") {
            displayDate = "Yesterday"
        } else {
            displayDate = dateLocal
        }
        return displayDate
    }
}