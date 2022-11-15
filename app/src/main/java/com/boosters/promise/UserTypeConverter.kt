package com.boosters.promise

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson

@ProvidedTypeConverter
class UserTypeConverter(private val gson: Gson) {

    @TypeConverter
    fun listToJson(value: List<User>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun jsonToList(value: String): List<User>? {
        return gson.fromJson(value, Array<User>::class.java)?.toList()
    }

}