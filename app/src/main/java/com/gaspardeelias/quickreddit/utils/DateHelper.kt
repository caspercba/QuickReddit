package com.gaspardeelias.quickreddit.utils

import android.content.Context
import com.gaspardeelias.quickreddit.core.R
import org.joda.time.*

class DateHelper {

    companion object {

        fun getTimeAgo(
            context: Context,
            date: Long
        ): String? { //date = date * 1000;
            val compareDate = DateTime(date)
            val now = DateTime()
            var time: String? = null
            val duration = Duration(compareDate, now)
            //Now
            time = if (now.minusMinutes(1).isBefore(date)) {
                context.getString(R.string.status_time_now)
                // A minute ago
            } else if (now.minusMinutes(2).isBefore(date)) {
                String.format("1 %s", context.getString(R.string.status_time_minute))
                //minutes ago
            } else if (now.minusHours(1).isBefore(date)) {
                val minutes = duration.standardMinutes
                String.format(
                    "%d %s",
                    minutes,
                    context.getString(R.string.status_time_minutes)
                )
                //an hour ago
            } else if (now.minusHours(2).isBefore(date)) {
                String.format("1 %s", context.getString(R.string.status_time_hour))
                //hours ago
            } else if (now.minusDays(1).isBefore(date)) {
                val hours = duration.standardHours
                String.format(
                    "%d %s",
                    hours,
                    context.getString(R.string.status_time_hours)
                )
                //day ago
            } else if (now.minusDays(2).isBefore(date)) {
                String.format("1 %s", context.getString(R.string.status_time_day))
                //days ago
            } else if (now.minusMonths(1).isBefore(date)) {
                val days = duration.standardDays
                String.format(
                    "%d %s",
                    days,
                    context.getString(R.string.status_time_days)
                )
                //month ago
            } else if (now.minusMonths(2).isBefore(date)) {
                String.format("1 %s", context.getString(R.string.status_time_month))
                //months ago
            } else if (now.minusYears(1).isBefore(date)) {
                val months =
                    Months.monthsBetween(compareDate.toLocalDate(), now.toLocalDate()).months
                String.format(
                    "%d %s",
                    months,
                    context.getString(R.string.status_time_months)
                )
                //a year ago
            } else if (now.minusYears(2).isBefore(date)) {
                "1 year ago"
                //years ago
            } else {
                val years =
                    Years.yearsBetween(compareDate.toLocalDate(), now.toLocalDate()).years
                String.format(
                    "%d %s",
                    years,
                    context.getString(R.string.status_time_years)
                )
            }
            return time
        }

    }
}