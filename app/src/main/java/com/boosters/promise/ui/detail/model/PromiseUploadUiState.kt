package com.boosters.promise.ui.detail.model

sealed class PromiseUploadUiState(val id: String) {

    class Accept(id: String, val delayMillisFromCurrentTime: Long) : PromiseUploadUiState(id)

    class Denied(id: String) : PromiseUploadUiState(id)

}