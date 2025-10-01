package com.mvpbrosproduction.simpleexpensetracker.composables.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvpbrosproduction.simpleexpensetracker.composables.inputs.ExpenseCategoryDropdown
import com.mvpbrosproduction.simpleexpensetracker.composables.inputs.NumberInput
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategory
import com.mvpbrosproduction.simpleexpensetracker.data_models.Expense
import com.mvpbrosproduction.simpleexpensetracker.managers.ExpenseCategoryManager

private const val TAG = "EditExpenseDialog"

@Composable
public fun EditExpenseDialog(
    expense: Expense,
    editExpense: (Expense) -> Unit,
    categoryManager: ExpenseCategoryManager,
    dialogOpen: MutableState<Boolean>
) {
    val editedExpenseAmount = remember {
        mutableDoubleStateOf(expense.amount)
    }

    val editedExpenseCategory = remember {
        mutableStateOf(expense.category)
    }

    val editedExpenseDescription = remember {
        mutableStateOf(expense.description)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AlertDialog(
            onDismissRequest = {
                dialogOpen.value = false
            },
            confirmButton = {
                TextButton(onClick = {
                    val editedExpense = Expense(
                        expense.id,
                        expense.day,
                        expense.month,
                        expense.year,
                        editedExpenseAmount.doubleValue,
                        editedExpenseCategory.value,
                        editedExpenseDescription.value
                    )
                    editExpense(editedExpense)
                }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    dialogOpen.value = false
                }) {
                    Text(text = "CANCEL")
                }
            },
            text = {
                Column {
                    Text(
                        text = "Edit expense",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Row(
                        modifier = Modifier.padding(0.dp, 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Amount:", modifier = Modifier.width(72.dp))
                        NumberInput(initValue = editedExpenseAmount.doubleValue) { newAmount ->
                            editedExpenseAmount.doubleValue = newAmount
                        }
                    }
                    Row(
                        modifier = Modifier.padding(0.dp, 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Category:", modifier = Modifier.width(72.dp))
                        ExpenseCategoryDropdown(
                            modifier = Modifier.background(color = Color.White),
                            categoryManager = categoryManager,
                            value = editedExpenseCategory.value,
                            size = 36.dp
                        ) { selectedCategory ->
                            editedExpenseCategory.value = selectedCategory
                        }
                    }
                    Column(modifier = Modifier.padding(0.dp, 8.dp)) {
                        Text(text = "Description:", modifier = Modifier.padding(bottom = 8.dp))
                        TextField(
                            value = editedExpenseDescription.value ?: "",
                            onValueChange = { text ->
                                editedExpenseDescription.value = text
                            },
                            minLines = 2,
                            maxLines = 3,
                            modifier = Modifier
                                .padding(bottom = 20.dp)
                        )
                    }
                }
            },
            containerColor = Color.White
        )
    }
}