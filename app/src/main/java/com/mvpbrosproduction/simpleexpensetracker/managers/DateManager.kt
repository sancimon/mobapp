package com.mvpbrosproduction.simpleexpensetracker.managers

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mvpbrosproduction.simpleexpensetracker.models.SimpleDate
import java.util.Date

private const val TAG = "DateManager"

class DateManager private constructor() {

    public var selectedDate: SimpleDate = SimpleDate(Date())

    public val isToday: MutableState<Boolean> = mutableStateOf(true)

    private val _dateChangeListeners = mutableListOf<(SimpleDate) -> Unit>()
    private val _monthChangeListeners = mutableListOf<(SimpleDate) -> Unit>()

    private val _todayDate = SimpleDate(Date())

    companion object {
        @Volatile
        private var _instance: DateManager? = null

        public fun getInstance() =
            _instance ?: synchronized(this) {
                _instance ?: DateManager().also { _instance = it }
            }
    }

    public fun setDate(date: Date) {
        val newlySelectedDate = SimpleDate(date)

        if (selectedDate == newlySelectedDate) {
            Log.d(TAG, "Same date selected: ${newlySelectedDate.displayDate}")
            return
        }

        isToday.value = _isSelectedDateToday(newlySelectedDate)

        selectedDate = newlySelectedDate

        _dateChangeListeners.forEach { it(newlySelectedDate) }

        // check if month has changed
        if (newlySelectedDate.month == selectedDate.month &&
            newlySelectedDate.year == selectedDate.year)
        {
            Log.d(TAG, "Same month selected: ${newlySelectedDate.month}")
            return
        }

        _monthChangeListeners.forEach { it(newlySelectedDate) }
    }

    public fun addDateChangeListener(listener: (SimpleDate) -> Unit) {
        _dateChangeListeners.add(listener)
    }

    public fun addMonthChangeListener(listener: (SimpleDate) -> Unit) {
        _monthChangeListeners.add(listener)
    }

    private fun _isSelectedDateToday(newlySelectedDate: SimpleDate): Boolean {
        return _todayDate == newlySelectedDate
    }

}