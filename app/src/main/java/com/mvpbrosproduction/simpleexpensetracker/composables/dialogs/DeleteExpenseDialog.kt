package com.mvpbrosproduction.simpleexpensetracker.composables.dialogs

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mvpbrosproduction.simpleexpensetracker.data_models.Expense

private const val TAG = "DeleteExpenseDialog"

@Composable
fun DeleteExpenseDialog(
    expense: Expense,
    deleteExpense: (Expense) -> Unit,
    dialogOpen: MutableState<Boolean>
) {
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
                    deleteExpense(expense)
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
                Text(
                    text = "Delete expense?", fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            },
            containerColor = Color.White
        )
    }
}