package com.mvpbrosproduction.simpleexpensetracker.composables.pages

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.mvpbrosproduction.simpleexpensetracker.composables.inputs.ExpenseCategoryDropdown
import com.mvpbrosproduction.simpleexpensetracker.composables.inputs.NumberInput
import com.mvpbrosproduction.simpleexpensetracker.composables.ui.DatePickerBtn
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategory
import com.mvpbrosproduction.simpleexpensetracker.managers.DateManager
import com.mvpbrosproduction.simpleexpensetracker.managers.ExpenseCategoryManager
import com.mvpbrosproduction.simpleexpensetracker.managers.ExpenseManager
import com.mvpbrosproduction.simpleexpensetracker.data_models.Expense
import com.mvpbrosproduction.simpleexpensetracker.models.SimpleDate
import com.mvpbrosproduction.simpleexpensetracker.ui.theme.SimpleExpenseTrackerTheme
import kotlinx.coroutines.launch

private const val TAG = "AddExpensePage"

@Composable
fun AddExpensePage(
    categoryManager: ExpenseCategoryManager,
    expenseManager: ExpenseManager,
    onExpenseAdded: () -> Unit
) {
    var amount by remember { mutableDoubleStateOf(0.0) }

    var category by remember { mutableStateOf<ExpenseCategory?>(null) }

    val coroutineScope = rememberCoroutineScope()

    val createExpense: () -> Unit = {
        coroutineScope.launch {
            val selectedDate: SimpleDate = DateManager.getInstance().selectedDate

            val newExpense = Expense(
                day = selectedDate.day,
                month = selectedDate.month,
                year = selectedDate.year,
                amount = amount.toDouble(),
                category = category,
            )

            expenseManager.addExpense(newExpense)

            onExpenseAdded()
        }
    }

    SimpleExpenseTrackerTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 32.dp)
            ) {
                DatePickerBtn()
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 120.dp)
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Amount:")
                    NumberInput(
                        initValue = amount,
                        modifier = Modifier.fillMaxWidth(0.8f).height(54.dp)
                    ) { newAmount ->
                        amount = newAmount
                    }
                }

                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(text = "Category:")
                    ExpenseCategoryDropdown(
                        categoryManager = categoryManager,
                        modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .background(color = Color.White)
                    ) { selectedCategory ->
                        category = selectedCategory
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
                        createExpense()
                    },
                    shape = RectangleShape,
                    enabled = amount != 0.0 && category != null,
                    modifier = Modifier.wrapContentSize()
                ) {
                    Text(text = "CREATE")
                }
            }
        }
    }
}