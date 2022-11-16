package com.boosters.promise.data.promise.source.remote

import com.google.gson.annotations.SerializedName

data class PlaceResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("display") val display: Int,
    @SerializedName("items") val items: List<PlaceItemResponse?>
)

data class PlaceItemResponse(
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