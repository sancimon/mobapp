package com.mvpbrosproduction.simpleexpensetracker.composables.lists

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mvpbrosproduction.simpleexpensetracker.composables.list_entries.MonthlyExpenseListEntry
import com.mvpbrosproduction.simpleexpensetracker.constants.NavigationTarget
import com.mvpbrosproduction.simpleexpensetracker.enums.DailyExpenseStatus
import com.mvpbrosproduction.simpleexpensetracker.models.DailyExpense
import com.mvpbrosproduction.simpleexpensetracker.models.MonthlyExpense
import kotlinx.coroutines.launch

private const val TAG = "MonthlyExpensesList"

@Composable
public fun MonthlyExpensesList(
    monthlyExpense: MonthlyExpense,
    dailyExpenseLimit: Double,
    navController: NavController,
    snackbarHostState: SnackbarHostState,
) {
    fun dailyExpenseStatusMessage(dailyExpense: DailyExpense): String {
        return when (dailyExpense.status) {
            DailyExpenseStatus.NOT_EXCEEDED ->
                "Congrats! You have not exceeded daily expenses limit :) You saved ${dailyExpenseLimit - dailyExpense.total} today!"

            DailyExpenseStatus.EXCEEDED ->
                "You have exceeded daily expenses limit on this day. Try to save up in following days :)"

            DailyExpenseStatus.EXCEEDED_PREVIOUSLY ->
                "Limit was exceeded some days before. Your daily limit for today should be ${dailyExpense.allowedSpending} :)"
        }
    }

    val scope = rememberCoroutineScope()

    val onStatusIndicatorClicked: (expense: DailyExpense) -> Unit = { expense ->
        val statusMessage: String = dailyExpenseStatusMessage(expense)

        scope.launch {
            snackbarHostState.showSnackbar(statusMessage)
        }
    }

    val onGoToDailyExpenseClicked: () -> Unit = {
        navController.navigate(NavigationTarget.DAILY_EXPENSES)
    }

    LazyColumn(
        modifier = Modifier.run {
            padding(horizontal = 24.dp, vertical = 16.dp)
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.primary
                )
        }

    ) {

        itemsIndexed(monthlyExpense.dailyExpenses) { index, dailyExpense ->

            MonthlyExpenseListEntry(
                dailyExpense = dailyExpense,
                onStatusIndicatorClicked = onStatusIndicatorClicked,
                onGoToDailyExpenseClicked = onGoToDailyExpenseClicked,
                modifier = Modifier.background(
                    if (index % 2 == 0) Color(
                        240,
                        240,
                        240
                    ) else Color.White
                )
            )
        }
    }


}