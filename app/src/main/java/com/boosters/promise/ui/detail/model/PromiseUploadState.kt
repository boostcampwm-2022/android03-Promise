package com.boosters.promise.ui.detail.model

sealed class PromiseUploadState(val id: String) {

    class Accept(id: String, val dateAndTime: String) : PromiseUploadState(id)

    class Denied(id: String) : PromiseUploadState(id)

}