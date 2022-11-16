package com.boosters.promise.util

import androidx.core.text.parseAsHtml
import com.boosters.promise.data.promise.Place
import com.boosters.promise.data.promise.source.remote.PlaceItemResponseBody
import com.boosters.promise.ui.place.PlaceUiState

object PlaceMapper {
    fun PlaceItemResponseBody.toPlace(): Place {
        val title = this.title.parseAsHtml().toString()
        return Place(
            placeTitle = title,
            link = link,
            address = address,
            roadAddress = roadAddress,
            x = mapx,
            y = mapy
        )
    }

    fun Place.toPlaceUiModel(): PlaceUiState {
        return PlaceUiState(
            title = placeTitle,
            link = link,
            address = address,
            roadAddress = roadAddress,
            x = x,
            y = y
        )
    }

    fun PlaceUiState.toPlace(): Place {
        return Place(
            placeTitle = title,
            link = link,
            address = address,
            roadAddress = roadAddress,
            x = x,
            y = y
        )
    }
}