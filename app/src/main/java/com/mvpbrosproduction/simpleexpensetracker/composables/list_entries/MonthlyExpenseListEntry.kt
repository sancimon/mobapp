package com.mvpbrosproduction.simpleexpensetracker.composables.list_entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mvpbrosproduction.simpleexpensetracker.enums.DailyExpenseStatus
import com.mvpbrosproduction.simpleexpensetracker.managers.DateManager
import com.mvpbrosproduction.simpleexpensetracker.models.DailyExpense
import java.util.Date

private const val TAG = "MonthlyExpenseListEntry"

@Composable
fun MonthlyExpenseListEntry(
    dailyExpense: DailyExpense,
    onStatusIndicatorClicked: (dailyExpense: DailyExpense) -> Unit,
    onGoToDailyExpenseClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dateManager: DateManager = DateManager.getInstance()

    val dailyExpenseStatusColor = _dailyExpenseStatusColor(dailyExpense.status)

    val humanReadableExpenseStatus: String = when (dailyExpense.status) {
        DailyExpenseStatus.NOT_EXCEEDED -> "Not exceeded"
        DailyExpenseStatus.EXCEEDED_PREVIOUSLY -> "Exceeded some days before"
        DailyExpenseStatus.EXCEEDED -> "Exceeded"
    }

    Row(
        modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row {
            Text(
                modifier = Modifier
                    .fillMaxWidth(0.4f)
                    .padding(start = 16.dp),
                text = dailyExpense.date.displayDate
            )
            Text(
                modifier = Modifier.fillMaxWidth(0.4f),
                textAlign = TextAlign.Start,
                text = "${dailyExpense.total}"
            )
        }
        Row {
            IconButton(onClick = {
                onStatusIndicatorClicked(dailyExpense)
            }) {
                Icon(
                    imageVector = Icons.Filled.Email,
                    tint = dailyExpenseStatusColor,
                    contentDescription = "On date: ${dailyExpense.date.displayDate} expenses limit is: $humanReadableExpenseStatus"
                )
            }
            IconButton(onClick = {
                dateManager.setDate(Date(dailyExpense.date.msSinceEpoch))

                onGoToDailyExpenseClicked()
            }) {
                Icon(
                    imageVector = Icons.Filled.ArrowForwardIos,
                    contentDescription = "Go to daily expense page for: ${dailyExpense.date.displayDate}"
                )
            }
        }
    }
}

private fun _dailyExpenseStatusColor(status: DailyExpenseStatus): Color {
    return when (status) {
        DailyExpenseStatus.NOT_EXCEEDED -> Color.Green
        DailyExpenseStatus.EXCEEDED_PREVIOUSLY -> Color(255, 165, 0)
        DailyExpenseStatus.EXCEEDED -> Color.Red
    }
}

