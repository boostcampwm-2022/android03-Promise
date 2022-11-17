package com.boosters.promise.data.promise

import com.boosters.promise.ui.promise.model.PromiseUiState

data class Promise(
    val promiseId: String,
    val title: String,
    val destinationName: String,
    val destinationLocation: Location?,
    val date: String,
    val time: String,
    val members: List<String>
)

fun Promise.toPromiseUiState() =
    PromiseUiState(
        promiseId = promiseId,
        title = title,
        destinationName = destinationName,
        destinationLocation = destinationLocation,
        date = date,
        time = time,
        members = members
    )

fun Promise.toPromiseBody() =
    PromiseBody(
        promiseId = promiseId,
        title = title,
        destinationName = destinationName,
        destinationLocation = destinationLocation,
        date = date,
        time = time,
        members = members
    )