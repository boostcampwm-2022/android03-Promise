package com.boosters.promise.ui.promise

/*sealed class aEvent {
    data class ShowDatePicker(val unit: Unit = Unit): Event()
    data class ShowTimePicker(val unit: Unit = Unit): Event()
    data class ShowPlaceSearchDialog(val unit: Unit = Unit): Event()
}*/
enum class Event {
    ShowDatePicker, ShowTimePicker, ShowPlaceSearchDialog
}