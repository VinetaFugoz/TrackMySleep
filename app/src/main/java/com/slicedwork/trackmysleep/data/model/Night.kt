package com.slicedwork.trackmysleep.data.model

data class Night(
    var nightId: String,
    var startTime: String,
    var endTime: String,
    var duration: String,
    var sleepQuality: String,
    var sleepQualityIcon: Int,
)
