package com.mvpbrosproduction.simpleexpensetracker.composables.lists

import DailyExpensesListEmpty
import android.util.Log
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mvpbrosproduction.simpleexpensetracker.composables.dialogs.DeleteExpenseDialog
import com.mvpbrosproduction.simpleexpensetracker.composables.dialogs.EditExpenseDialog
import com.mvpbrosproduction.simpleexpensetracker.composables.ui.ExpenseCategoryIcon
import com.mvpbrosproduction.simpleexpensetracker.managers.ExpenseCategoryManager
import com.mvpbrosproduction.simpleexpensetracker.managers.ExpenseManager
import com.mvpbrosproduction.simpleexpensetracker.models.DailyExpense
import com.mvpbrosproduction.simpleexpensetracker.data_models.Expense

private const val TAG = "DailyExpensesList"

@Composable
fun DailyExpensesList(
    dailyExpense: DailyExpense,
    expenseManager: ExpenseManager,
    categoryManager: ExpenseCategoryManager
) {
    val isDeleteExpenseDialogOpen = remember { mutableStateOf(false) }
    val isEditExpenseDialogOpen = remember { mutableStateOf(false) }

    val selectedExpense: MutableState<Expense?> = remember {
        mutableStateOf(null)
    }

    val deleteExpense: (Expense) -> Unit = { expense: Expense ->
        val success: Boolean = expenseManager.deleteExpense(expense)

        if (!success) {
            Log.d(TAG, "Failed to delete expense $expense")
        }
        isDeleteExpenseDialogOpen.value = false
    }

    val editExpense: (Expense) -> Unit = { expense: Expense ->
        val success: Boolean = expenseManager.editExpense(expense)

        if (!success) {
            Log.d(TAG, "Failed to update expense with id ${expense.id}")
        }
        isEditExpenseDialogOpen.value = false
    }

    LazyColumn(
        modifier = Modifier.run {
            padding(horizontal = 24.dp, vertical = 16.dp)
                .border(width = 1.dp, color = MaterialTheme.colorScheme.primary)
        }
    ) {
        item {
            if (dailyExpense.list.isEmpty()) {
                DailyExpensesListEmpty()
            }
        }
        items(dailyExpense.list) { expense ->
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                ExpenseCategoryIcon(
                    category = expense.category,
                    modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "${expense.amount}"
                )
                Row {
                    IconButton(onClick = {
                        selectedExpense.value = expense
                        isEditExpenseDialogOpen.value = true
                    }) {
                        Icon(Icons.Sharp.Edit, "Edit expense")
                    }
                    IconButton(onClick = {
                        selectedExpense.value = expense
                        isDeleteExpenseDialogOpen.value = true
                    }) {
                        Icon(Icons.Sharp.Delete, "Delete expense")
                    }
                }
            }
        }
    }

    if (isDeleteExpenseDialogOpen.value) {
        DeleteExpenseDialog(
            expense = selectedExpense.value!!,
            deleteExpense = deleteExpense,
            dialogOpen = isDeleteExpenseDialogOpen
        )
    }

    if (isEditExpenseDialogOpen.value) {
        EditExpenseDialog(
            expense = selectedExpense.value!!,
            editExpense = editExpense,
            categoryManager = categoryManager,
            dialogOpen = isEditExpenseDialogOpen
        )
    }
}

