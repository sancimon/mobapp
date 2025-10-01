package com.mvpbrosproduction.simpleexpensetracker.composables.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.mvpbrosproduction.simpleexpensetracker.composables.DatePicker
import com.mvpbrosproduction.simpleexpensetracker.managers.DateManager
import com.mvpbrosproduction.simpleexpensetracker.models.SimpleDate

@Composable
fun DatePickerBtn() {

    val dateManager: DateManager = DateManager.getInstance()

    val selectedDate: SimpleDate = dateManager.selectedDate

    val isDatePickerOpen = remember {
        mutableStateOf(false)
    }

    Button(
        onClick = {
            isDatePickerOpen.value = !isDatePickerOpen.value
        },
        shape = RectangleShape,
        modifier = Modifier.wrapContentSize()
    ) {
        Text(text = selectedDate.displayDate)

        if (dateManager.isToday.value) {
            Icon(
                Icons.Filled.Star,
                contentDescription = "is today",
                modifier = Modifier.padding(start = 8.dp).size(20.dp)
            )
        }
    }


    // handle visibility of the date picker
    if (isDatePickerOpen.value) {
        DatePicker(
            isCalendarOpen = isDatePickerOpen,
            dateManager = dateManager,
        )
    }
}