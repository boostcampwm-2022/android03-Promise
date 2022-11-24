package com.boosters.promise.ui.promisesetting.model

import android.os.Parcelable
import com.boosters.promise.data.model.Location
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.ui.invite.model.UserUiState
import com.boosters.promise.ui.invite.model.toUser
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromiseUiState(
    val promiseId: String = "",
    val title: String = "",
    val destinationName: String = "",
    val destinationLocation: Location = Location(),
    val date: String = "",
    val time: String = "",
    val members: List<UserUiState> = listOf()
) : Parcelable

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