package com.boosters.promise.util

import androidx.core.text.parseAsHtml
import com.boosters.promise.Local
import com.boosters.promise.network.LocalItemResponse

object LocalMapper {
    fun LocalItemResponse.asLocal(): Local {
        val title = this.title.parseAsHtml().toString()
        return Local(
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