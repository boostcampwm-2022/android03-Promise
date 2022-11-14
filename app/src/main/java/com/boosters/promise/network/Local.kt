package com.boosters.promise.network

import com.google.gson.annotations.SerializedName

data class LocalResponse(
    @SerializedName("total") val total: Int,
    @SerializedName("start") val start: Int,
    @SerializedName("display") val display: Int,
    @SerializedName("items") val items: List<LocalItemResponse?>
)

data class LocalItemResponse(
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