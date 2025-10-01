package com.mvpbrosproduction.simpleexpensetracker.composables.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mvpbrosproduction.simpleexpensetracker.composables.lists.MonthlyExpensesList
import com.mvpbrosproduction.simpleexpensetracker.managers.ExpenseManager
import com.mvpbrosproduction.simpleexpensetracker.models.MonthlyExpense
import com.mvpbrosproduction.simpleexpensetracker.ui.theme.SimpleExpenseTrackerTheme

private const val TAG = "MonthlyExpensesPage"

@Composable
fun MonthlyExpensesPage(
    expenseManager: ExpenseManager,
    snackbarHostState: SnackbarHostState,
    navController: NavController,
    onBackBtnClick: () -> Unit,
    onFwdBtnClick: () -> Unit
) {
    val monthlyExpense: MonthlyExpense = expenseManager.monthlyExpense.value

    SimpleExpenseTrackerTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 32.dp, bottom = 32.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(0.9f)
                ) {
                    MonthlyExpensesList(
                        monthlyExpense = monthlyExpense,
                        dailyExpenseLimit = expenseManager.dailyExpenseLimit.doubleValue,
                        snackbarHostState = snackbarHostState,
                        navController = navController
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
                        onFwdBtnClick()
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


