package com.boosters.promise.data.place.source.remote

import androidx.core.text.parseAsHtml
import com.boosters.promise.data.place.Place
import com.google.gson.annotations.SerializedName

data class PlaceResponseBody(
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("display") val display: Int,
    @SerializedName("items") val items: List<PlaceItemResponseBody?>
)

data class PlaceItemResponseBody(
    @SerializedName("title") val title: String,
    @SerializedName("link") val link: String,
    @SerializedName("category") val category: String,
    @SerializedName("description") val description: String,
    @SerializedName("telephone") val telephone: String,
    @SerializedName("address") val address: String,
    @SerializedName("roadAddress") val roadAddress: String,
    @SerializedName("mapx") val mapx: Int,
    @SerializedName("mapy") val mapy: Int
)

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