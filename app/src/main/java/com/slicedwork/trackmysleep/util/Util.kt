package com.slicedwork.trackmysleep.util

import android.annotation.SuppressLint
import android.content.res.Resources
import com.slicedwork.trackmysleep.R
import com.slicedwork.trackmysleep.data.model.Night
import com.slicedwork.trackmysleep.data.source.local.entity.SleepNight
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

private val ONE_MINUTE_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.MINUTES)
private val ONE_HOUR_MILLIS = TimeUnit.MILLISECONDS.convert(1, TimeUnit.HOURS)

fun convertDurationToFormatted(startTimeMilli: Long, endTimeMilli: Long, res: Resources): String {
    val durationMilli = endTimeMilli - startTimeMilli
    return when {
        durationMilli < ONE_MINUTE_MILLIS -> {
            val seconds = TimeUnit.SECONDS.convert(durationMilli, TimeUnit.MILLISECONDS)
            res.getString(R.string.seconds_length, seconds)
        }
        durationMilli < ONE_HOUR_MILLIS -> {
            val minutes = TimeUnit.MINUTES.convert(durationMilli, TimeUnit.MILLISECONDS)
            res.getString(R.string.minutes_length, minutes)
        }
        else -> {
            val hours = TimeUnit.HOURS.convert(durationMilli, TimeUnit.MILLISECONDS)
            res.getString(R.string.hours_length, hours)
        }
    }
}

fun convertNumericQualityToString(sleepQuality: Int, resources: Resources): String {
    return when (sleepQuality) {
        0 -> resources.getString(R.string.zero_very_bad)
        1 -> resources.getString(R.string.one_poor)
        2 -> resources.getString(R.string.two_soso)
        4 -> resources.getString(R.string.four_pretty_good)
        5 -> resources.getString(R.string.five_excellent)
        else -> "--"
    }
}

fun convertNumericQualityToDrawable(sleepQuality: Int): Int {
    return when (sleepQuality) {
        0 -> R.drawable.ic_sleep_0
        1 -> R.drawable.ic_sleep_1
        2 -> R.drawable.ic_sleep_2
        3 -> R.drawable.ic_sleep_3
        4 -> R.drawable.ic_sleep_4
        5 -> R.drawable.ic_sleep_5
        else -> R.drawable.ic_launcher_background
    }
}

@SuppressLint("SimpleDateFormat")
fun convertLongToDateString(systemTime: Long): String {
    return SimpleDateFormat("EEEE MMM-dd-yyyy' Time: 'HH:mm")
        .format(systemTime).toString()
}

fun formatNights(sleepNights: List<SleepNight>, resources: Resources): List<Night> {
    val nights: MutableList<Night> = mutableListOf()


    for (sleepNight in sleepNights) {
        val night = Night(
            sleepNight.nightId.toString(),
            convertLongToDateString(sleepNight.startTimeMilli),
            convertLongToDateString(sleepNight.endTimeMilli),
            convertDurationToFormatted(sleepNight.startTimeMilli, sleepNight.endTimeMilli, resources),
            convertNumericQualityToString(sleepNight.sleepQuality, resources),
            convertNumericQualityToDrawable(sleepNight.sleepQuality)
        )

        if (sleepNight.startTimeMilli == sleepNight.endTimeMilli) night.endTime =
            "--"

        nights.add(night)
    }

    return nights
}
