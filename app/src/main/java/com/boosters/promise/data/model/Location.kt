package com.boosters.promise.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Location(
    val x: Int = 0,
    val y: Int = 0
) : Parcelable