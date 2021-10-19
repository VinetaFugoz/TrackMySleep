package com.slicedwork.trackmysleep.ui.sleeptracker

import androidx.lifecycle.*
import com.slicedwork.trackmysleep.data.source.local.dao.SleepDatabaseDao
import com.slicedwork.trackmysleep.data.source.local.entity.SleepNight
import kotlinx.coroutines.launch

class SleepTrackerViewModel(
    private val database: SleepDatabaseDao
) : ViewModel() {

    private var tonightLiveData = MutableLiveData<SleepNight?>()

    private val _navigateToSleepQuality = MutableLiveData<SleepNight?>()
    val navigateToSleepQuality: MutableLiveData<SleepNight?> get() = _navigateToSleepQuality

    private var _showSnackbarEvent = MutableLiveData<Boolean>()
    val showSnackBarEvent: LiveData<Boolean> get() = _showSnackbarEvent

    var sleepNightsLiveData: LiveData<List<SleepNight>> = database.getAllNights()

    val isStartButtonEnable = Transformations.map(tonightLiveData) { it == null }
    val isStopButtonEnable = Transformations.map(tonightLiveData) { it != null }
    val isClearButtonEnable = Transformations.map(sleepNightsLiveData) { it?.isNotEmpty() }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        viewModelScope.launch {
            tonightLiveData.value = getTonightFromDataBase()
        }
    }

    // |-------------------|
    // | Handler Functions |
    // |-------------------|
    fun onStart() {
        viewModelScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonightLiveData.value = getTonightFromDataBase()
        }
    }

    fun onStop() {
        viewModelScope.launch {
            val oldNight = tonightLiveData.value ?: return@launch
            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }

    fun onClear() {
        viewModelScope.launch {
            clear()
            tonightLiveData.value = null
            _showSnackbarEvent.value = true
        }
    }

    // |-------------------|
    // | Suspend Functions |
    // |-------------------|
    private suspend fun getTonightFromDataBase(): SleepNight? {
        var tonight: SleepNight? = database.getTonight()
        if (isTonightAlreadyFinished(tonight)) {
            tonight = null
        }
        return tonight
    }

    private suspend fun update(tonight: SleepNight) {
        database.update(tonight)
    }

    private suspend fun insert(tonight: SleepNight) {
        database.insert(tonight)
    }

    private suspend fun clear() {
        database.clear()
    }

    // |-------------------------|
    // | Suspended Functions Aux |
    // |-------------------------|
    private fun isTonightAlreadyFinished(tonight: SleepNight?): Boolean {
        return tonight?.endTimeMilli != tonight?.startTimeMilli
    }

    // |--------------------|
    // | LiveData functions |
    // |--------------------|
    fun doneNavigating() {
        _navigateToSleepQuality.value = null
    }

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }
}

