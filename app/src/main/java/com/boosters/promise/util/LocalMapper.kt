package com.boosters.promise.util

import androidx.core.text.parseAsHtml
import com.boosters.promise.data.promise.Place
import com.boosters.promise.data.promise.source.remote.PlaceItemResponse

object PlaceMapper {
    fun PlaceItemResponse.asPlace(): Place {
        val title = this.title.parseAsHtml().toString()
        return Place(
            title = title,
            link = link,
            category = category,
            description = description,
            telephone = telephone,
            address = address,
            roadAddress = roadAddress,
            x = mapx,
            y = mapy
        )
    }
}