package com.boosters.promise.ui.detail.model

sealed class PromiseUploadUiState(val id: String) {

    class Accept(id: String, val dateAndTime: String) : PromiseUploadUiState(id)

    class Denied(id: String) : PromiseUploadUiState(id)

}