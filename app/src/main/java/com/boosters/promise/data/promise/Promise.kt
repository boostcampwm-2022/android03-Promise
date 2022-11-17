package com.boosters.promise.data.promise

import com.boosters.promise.ui.promise.model.PromiseUiState
import com.boosters.promise.data.user.User
import com.boosters.promise.data.user.toUserUiState

data class Promise(
    val promiseId: String,
    val title: String,
    val destinationName: String,
    val destinationLocation: Location?,
    val date: String,
    val time: String,
    val members: List<User>
)

fun Promise.toPromiseUiState() =
    PromiseUiState(
        promiseId = promiseId,
        title = title,
        destinationName = destinationName,
        destinationLocation = destinationLocation,
        date = date,
        time = time,
        members = members.map {
            it.toUserUiState()
        }
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