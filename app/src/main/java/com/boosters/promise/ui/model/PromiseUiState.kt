package com.boosters.promise.ui.model

import com.boosters.promise.data.promise.Location
import com.boosters.promise.data.promise.Promise

data class PromiseUiState(
    val promiseId: String,
    val title: String,
    val destinationName: String,
    val destinationLocation: Location?,
    val date: String,
    val time: String,
    val members: List<String>
)

fun PromiseUiState.toPromise() =
    Promise(
        promiseId = promiseId,
        title = title,
        destinationName = destinationName,
        destinationLocation = destinationLocation,
        date = date,
        time = time,
        members = members
    )