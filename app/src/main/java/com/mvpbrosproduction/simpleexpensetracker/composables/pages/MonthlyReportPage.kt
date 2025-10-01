package com.mvpbrosproduction.simpleexpensetracker.composables.pages

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvpbrosproduction.simpleexpensetracker.composables.ui.ExpenseProgressBar
import com.mvpbrosproduction.simpleexpensetracker.data_models.Expense
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategory
import com.mvpbrosproduction.simpleexpensetracker.managers.DateManager
import com.mvpbrosproduction.simpleexpensetracker.managers.ExpenseManager
import com.mvpbrosproduction.simpleexpensetracker.models.SimpleDate
import com.mvpbrosproduction.simpleexpensetracker.ui.theme.SimpleExpenseTrackerTheme
import java.time.YearMonth

private const val TAG = "YearlyExpensesPage"

@Composable
fun MonthlyReportPage(
    expenseManager: ExpenseManager,
    dailyExpenseLimit: Double,
    onBackBtnClick: () -> Unit,
) {
    val selectedDate: SimpleDate = DateManager.getInstance().selectedDate
    val monthlyExpenses: List<Expense> = expenseManager.monthlyExpense.value.list

    val daysInMonth = getDaysInMonth(selectedDate.year, selectedDate.month)
    val totalSpent: Double = monthlyExpenses.sumOf { expense -> expense.amount }
    val maxAllowedSpending: Double = daysInMonth * dailyExpenseLimit

    val expenseProgress: Double = if (dailyExpenseLimit != 0.0) {
        totalSpent / maxAllowedSpending
    } else {
        100.0
    }

    val expensesByCategory: List<Pair<ExpenseCategory, Double>> =
        groupExpensesByCategoryAndSortByTotal(monthlyExpenses)

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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Overview",
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Total Spent:")
                        Text(text = "$totalSpent", textAlign = TextAlign.End)
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Max. allowed spending:")
                        Text(text = "$maxAllowedSpending", textAlign = TextAlign.End)
                    }
                    ExpenseProgressBar(
                        expenseProgress.toFloat(), 8, modifier = Modifier.padding(bottom = 32.dp)
                    )
                    Text(
                        text = "By Category",
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
                    )
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        expensesByCategory.forEachIndexed { index, (category, categoryTotal) ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(if (index % 2 === 0) Color.LightGray else Color.White)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "${category.name.capitalizeFirstLetter()}",
                                        modifier = Modifier.weight(0.5f)
                                    )
                                    Text(text = "$categoryTotal", modifier = Modifier.weight(0.25f))
                                    Text(
                                        text = "${(categoryTotal / totalSpent * 100).toInt()} %",
                                        textAlign = TextAlign.End,
                                        modifier = Modifier.weight(0.25f)
                                    )
                                }
                            }
                        }
                    }
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
            }
        }
    }
}

private fun getDaysInMonth(year: Int, month: Int): Int {
    val yearMonth = YearMonth.of(year, month + 1) // Month is 1-based in java.time
    return yearMonth.lengthOfMonth()
}

private fun groupExpensesByCategoryAndSortByTotal(expenses: List<Expense>): List<Pair<ExpenseCategory, Double>> {
    val expensesByCategory: MutableMap<ExpenseCategory, Double> = mutableMapOf()

    for (expense in expenses) {
        val category = expense.category

        if (category !== null) {
            expensesByCategory[category] =
                expensesByCategory.getOrDefault(category, 0.0) + expense.amount
        }
    }
    return expensesByCategory.toList().sortedByDescending { (_, total) -> total }
}

private fun String.capitalizeFirstLetter(): String {
    return replaceFirstChar(Char::titlecase)
}
