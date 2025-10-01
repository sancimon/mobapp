package com.mvpbrosproduction.simpleexpensetracker.composables

import android.util.Log
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.viewinterop.AndroidView
import com.mvpbrosproduction.simpleexpensetracker.managers.DateManager
import com.mvpbrosproduction.simpleexpensetracker.models.SimpleDate
import java.util.Date

private const val TAG = "GeneralComposable"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePicker(
    isCalendarOpen: MutableState<Boolean>,
    dateManager: DateManager
) {
    val selectedDate: SimpleDate = dateManager.selectedDate

    Log.d(TAG, "DatePicker called with initial milliseconds ${selectedDate.msSinceEpoch}")

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = selectedDate.msSinceEpoch
    )

    val confirmEnabled = remember { derivedStateOf { datePickerState.selectedDateMillis != null } }

    DatePickerDialog(
        onDismissRequest = {
            isCalendarOpen.value = false
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (datePickerState.selectedDateMillis != null) {
                        dateManager.setDate(Date(datePickerState.selectedDateMillis!!))
                    }
                    isCalendarOpen.value = false
                },
                enabled = confirmEnabled.value
            ) {
                Text(text = "OK")
            }
        },
        dismissButton = {
            TextButton(onClick = { isCalendarOpen.value = false }) {
                Text(text = "CANCEL")
            }
        },
        content = {
            androidx.compose.material3.DatePicker(state = datePickerState)
        }
    )
}



@Composable
fun NumberPicker(
    digitsArray: SnapshotStateList<Int>,
    digitIndex: Int,
    min: Int,
    max: Int,
    onValueChange: () -> Unit
    ) {
    AndroidView(
        factory = { context ->
            android.widget.NumberPicker(context).apply {
                setOnValueChangedListener { _, _, currentValue ->
                    digitsArray[digitIndex] = currentValue
                    onValueChange()
                }
                minValue = min
                maxValue = max
            }
        },
        update = {
            it.value = digitsArray[digitIndex]
        },
    )
}
