package com.slicedwork.trackmysleep.ui.sleepquality

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.slicedwork.trackmysleep.data.source.local.dao.SleepDatabaseDao

class SleepQualityViewModelFactory(
    private val sleepNightKey: Long = 0L,
    private val database: SleepDatabaseDao
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepQualityViewModel::class.java))
            return SleepQualityViewModel(sleepNightKey, database) as T
        throw throw IllegalArgumentException("Unknown ViewModel class")
    }
}
