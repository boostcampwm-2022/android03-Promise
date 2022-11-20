package com.boosters.promise.ui.promise.model

import com.boosters.promise.data.promise.Location
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.ui.invite.model.UserUiState
import com.boosters.promise.ui.invite.model.toUser

data class PromiseUiState(
    val promiseId: String = "",
    val title: String = "",
    val destinationName: String = "",
    val destinationLocation: Location? = null,
    val date: String = "",
    val time: String = "",
    val members: List<UserUiState> = listOf()
)

fun PromiseUiState.toPromise() =
    Promise(
        promiseId = promiseId,
        title = title,
        destinationName = destinationName,
        destinationLocation = destinationLocation,
        date = date,
        time = time,
        members = members.map {
            it.toUser()
        }
    )