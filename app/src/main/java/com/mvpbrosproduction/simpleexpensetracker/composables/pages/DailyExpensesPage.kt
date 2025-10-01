package com.mvpbrosproduction.simpleexpensetracker.composables.pages

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.mvpbrosproduction.simpleexpensetracker.composables.lists.DailyExpensesList
import com.mvpbrosproduction.simpleexpensetracker.composables.ui.DatePickerBtn
import com.mvpbrosproduction.simpleexpensetracker.composables.ui.ExpenseProgressBar
import com.mvpbrosproduction.simpleexpensetracker.managers.ExpenseCategoryManager
import com.mvpbrosproduction.simpleexpensetracker.managers.ExpenseManager
import com.mvpbrosproduction.simpleexpensetracker.models.DailyExpense
import com.mvpbrosproduction.simpleexpensetracker.ui.theme.SimpleExpenseTrackerTheme

private const val TAG = "DailyExpensesPage"

@Composable
fun DailyExpensesPage(
    expenseManager: ExpenseManager,
    categoryManager: ExpenseCategoryManager,
    dailyExpenseLimit: Double,
    onNextBtnClick: () -> Unit,
    onBackBtnClick: () -> Unit
) {
    val dailyExpense: MutableState<DailyExpense> = expenseManager.dailyExpense

    val expenseProgress = if (dailyExpenseLimit != 0.0) {
        dailyExpense.value.total / dailyExpenseLimit
    } else {
        100
    }

    SimpleExpenseTrackerTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
        ) {
            Column {
                ExpenseProgressBar(expenseProgress.toFloat())
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 32.dp, bottom = 32.dp)
            ) {
                DatePickerBtn()
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxHeight(0.6f)
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(0.7f)
                ) {
                    DailyExpensesList(
                        dailyExpense = dailyExpense.value,
                        expenseManager = expenseManager,
                        categoryManager = categoryManager
                    )
                }
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Bottom,
                modifier = Modifier.padding(bottom = 32.dp)
            ) {
                Button(
                    onClick = {
                        onBackBtnClick()
                    },
                    shape = RectangleShape,
                    modifier = Modifier
                        .wrapContentHeight()
                        .width(182.dp)
                        .padding(horizontal = 32.dp)
                ) {
                    Text(text = "BACK")
                }
                Button(
                    onClick = {
                        onNextBtnClick()
                    },
                    shape = RectangleShape,
                    modifier = Modifier
                        .wrapContentHeight()
                        .width(182.dp)
                        .padding(horizontal = 32.dp)
                ) {
                    Text(text = "NEXT")
                }
            }
        }
    }
}