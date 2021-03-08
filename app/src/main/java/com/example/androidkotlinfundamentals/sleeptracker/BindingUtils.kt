package com.example.androidkotlinfundamentals.sleeptracker

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.androidkotlinfundamentals.R
import com.example.androidkotlinfundamentals.convertDurationToFormatted
import com.example.androidkotlinfundamentals.convertNumericQualityToString
import com.example.androidkotlinfundamentals.database.SleepNight

class BindingUtils {
    @BindingAdapter("sleepDurationFormatted")
    fun TextView.setSleepDurationFormatted(item: SleepNight) {
        text = convertDurationToFormatted(item.startTimeMilli, item.endTimeMilli, context.resources)
    }

    @BindingAdapter("sleepQualityString")
    fun TextView.setSleepQualityString(item: SleepNight) {
        text = convertNumericQualityToString(item.sleepQuality, context.resources)
    }
    @BindingAdapter("sleepImage")
    fun ImageView.setSleepImage(item: SleepNight) {
        setImageResource(when (item.sleepQuality) {
            0 -> R.drawable.ic_sleep_0
            1 -> R.drawable.ic_sleep_1
            2 -> R.drawable.ic_sleep_2
            3 -> R.drawable.ic_sleep_3
            4 -> R.drawable.ic_sleep_4
            5 -> R.drawable.ic_sleep_5
            else -> R.drawable.ic_sleep_active
        })
    }

}