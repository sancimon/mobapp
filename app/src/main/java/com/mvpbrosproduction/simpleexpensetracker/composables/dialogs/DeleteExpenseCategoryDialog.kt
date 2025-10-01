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
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategory

private const val TAG = "DeleteExpenseCategoryDialog"

@Composable
fun DeleteExpenseCategoryDialog(
    dialogOpen: MutableState<Boolean>,
    category: ExpenseCategory,
    deleteCategory: (category: ExpenseCategory) -> Unit
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
                    deleteCategory(category)

                    dialogOpen.value = false
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
                Text(text = "Delete category?", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            },
            containerColor = Color.White
        )
    }
}