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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.mvpbrosproduction.simpleexpensetracker.data_models.StockPrice
import com.mvpbrosproduction.simpleexpensetracker.managers.DateManager
import com.mvpbrosproduction.simpleexpensetracker.managers.ExpenseManager
import com.mvpbrosproduction.simpleexpensetracker.models.SimpleDate
import com.mvpbrosproduction.simpleexpensetracker.repositories.StockRepository
import com.mvpbrosproduction.simpleexpensetracker.ui.theme.SimpleExpenseTrackerTheme
import kotlinx.coroutines.launch
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

    // S&P 500 stock price state
    var stockPrice by remember { mutableStateOf<StockPrice?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()
    val stockRepository = remember { StockRepository() }

    // Fetch stock price when composable is first displayed
    LaunchedEffect(Unit) {
        isLoading = true
        scope.launch {
            val result = stockRepository.getSP500Price()
            result.onSuccess { price ->
                stockPrice = price
                errorMessage = null
            }.onFailure { error ->
                errorMessage = error.message ?: "Failed to load stock price"
                Log.e(TAG, "Error loading stock price", error)
            }
            isLoading = false
        }
    }

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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            ) {
                // S&P 500 Stock Price Display
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 32.dp, vertical = 16.dp)
                        .background(Color(0xFFF5F5F5))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isLoading -> {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(modifier = Modifier.width(20.dp))
                                Text(text = "Loading Bitcoin price...", fontSize = 14.sp)
                            }
                        }
                        errorMessage != null -> {
                            Text(
                                text = "Bitcoin: $errorMessage",
                                color = Color.Red,
                                fontSize = 12.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        stockPrice != null -> {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(
                                    text = stockPrice!!.symbol,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "$${String.format("%.2f", stockPrice!!.price)}",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 20.sp,
                                    color = Color(0xFF1976D2)
                                )
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    val changeColor = if (stockPrice!!.change >= 0) Color(0xFF4CAF50) else Color(0xFFF44336)
                                    Text(
                                        text = "${if (stockPrice!!.change >= 0) "+" else ""}${String.format("%.2f", stockPrice!!.change)}",
                                        color = changeColor,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "(${if (stockPrice!!.changePercent >= 0) "+" else ""}${String.format("%.2f", stockPrice!!.changePercent)}%)",
                                        color = changeColor,
                                        fontSize = 14.sp
                                    )
                                }
                                Text(
                                    text = "Last updated: ${stockPrice!!.lastUpdated}",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    }
                }

                // Back Button
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
