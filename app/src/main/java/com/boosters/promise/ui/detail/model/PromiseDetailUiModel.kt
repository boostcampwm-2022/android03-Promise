package com.boosters.promise.ui.detail.model

import com.boosters.promise.data.location.GeoLocation

data class PromiseDetailUiModel(
    val userCode: String,
    val userName: String,
    val geoLocation: GeoLocation?,
    val isArrived: Boolean = false
)