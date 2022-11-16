package com.boosters.promise.data.promise.source.remote

import com.boosters.promise.ui.model.Promise

interface PromiseRepository {

    fun addPromise(promise: Promise)

    fun removePromise(promise: Promise)

    suspend fun getPromiseList(date: String): MutableList<Promise>

}