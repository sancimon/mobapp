package com.mvpbrosproduction.simpleexpensetracker.composables.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.mvpbrosproduction.simpleexpensetracker.composables.ExpenseCategoryPicker
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategory
import com.mvpbrosproduction.simpleexpensetracker.managers.ExpenseCategoryManager

@Composable
fun ChooseExpenseCategoryDialog(
    categoryManager: ExpenseCategoryManager,
    onClose: () -> Unit,
    onConfirm: (ExpenseCategory) -> Unit
) {
    val favoriteCategories = categoryManager.categories.value.filter { c -> c.isFavorite }
    val nonFavoriteCategories = categoryManager.categories.value.filter { c -> !c.isFavorite }

    val selectedExpenseCategory = remember { mutableStateOf(favoriteCategories.first()) }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = onClose,
            confirmButton = {
                TextButton(onClick = {
                    onConfirm(selectedExpenseCategory.value)
                }) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = onClose) {
                    Text(text = "CANCEL")
                }
            },
            text = {
                ExpenseCategoryPicker(
                    favoriteCategories = favoriteCategories,
                    nonFavoriteCategories = nonFavoriteCategories,
                    onCategorySelected = { selectedExpenseCategory.value = it}
                )
            },
        )
    }
}