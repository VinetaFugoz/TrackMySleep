package com.slicedwork.trackmysleep.ui.sleepquality

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.slicedwork.trackmysleep.data.source.local.dao.SleepDatabaseDao
import kotlinx.coroutines.launch

class SleepQualityViewModel(
    private val sleepNightKey: Long = 0L,
    private val database: SleepDatabaseDao
) : ViewModel() {
    private val _navigateToSleepTracker = MutableLiveData<Boolean?>()
    val navigateToSleepTracker: LiveData<Boolean?> get() = _navigateToSleepTracker
    // |------------------|
    // | Handler Function |
    // |------------------|
    fun onSetSleepQuality(quality: Int) {
        viewModelScope.launch {
            val tonight = database.get(sleepNightKey)
            tonight.sleepQuality = quality
            database.update(tonight)
            _navigateToSleepTracker.value = true
        }
    }
    // |--------------|
    // | Nav function |
    // |--------------|
    fun doneNavigating() {
        _navigateToSleepTracker.value = null
    }
}