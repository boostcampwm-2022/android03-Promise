package com.boosters.promise.ui.promisesetting.model

import com.boosters.promise.data.location.GeoLocation
import android.os.Parcelable
import com.boosters.promise.data.promise.Promise
import com.boosters.promise.ui.invite.model.UserUiState
import com.boosters.promise.ui.invite.model.toUser
import kotlinx.parcelize.Parcelize

@Parcelize
data class PromiseUiState(
    val promiseId: String = "",
    val title: String = "",
    val destinationName: String = "",
    val destinationGeoLocation: GeoLocation = GeoLocation(),
    val date: String = "",
    val time: String = "",
    val members: List<UserUiState> = listOf()
) : Parcelable

fun PromiseUiState.toPromise() =
    Promise(
        promiseId = promiseId,
        title = title,
        destinationName = destinationName,
        destinationGeoLocation = destinationGeoLocation,
        date = date,
        time = time,
        members = members.map {
            it.toUser()
        }
    )