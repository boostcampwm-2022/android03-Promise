package com.boosters.promise.ui.promisecalendar.decorator

import android.graphics.Color
import android.text.style.ForegroundColorSpan
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class PromiseDayAfterDecorator(
    private val startDay: CalendarDay = CalendarDay.today()
) : DayViewDecorator {

    override fun shouldDecorate(day: CalendarDay): Boolean {
        return day.year <= startDay.year &&
            (day.month < startDay.month || (day.month == startDay.month && day.day < startDay.day))
    }

    override fun decorate(view: DayViewFacade) {
        view.addSpan(ForegroundColorSpan(Color.LTGRAY))
        view.setDaysDisabled(true)
    }

}