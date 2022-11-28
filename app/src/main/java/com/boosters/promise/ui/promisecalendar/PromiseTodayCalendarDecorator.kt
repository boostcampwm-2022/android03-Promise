package com.boosters.promise.ui.promisecalendar

import android.graphics.drawable.Drawable
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class PromiseTodayCalendarDecorator(
    private val drawable: Drawable?
) : DayViewDecorator {

    private var today = CalendarDay.today()

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day == today
    }

    override fun decorate(view: DayViewFacade) {
        if (drawable != null) {
            view.setBackgroundDrawable(drawable)
        }
    }

}