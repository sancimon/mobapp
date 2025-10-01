package com.mvpbrosproduction.simpleexpensetracker.composables.list_entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Delete
import androidx.compose.material.icons.sharp.Edit
import androidx.compose.material.icons.sharp.Favorite
import androidx.compose.material.icons.sharp.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mvpbrosproduction.simpleexpensetracker.composables.dialogs.DeleteExpenseCategoryDialog
import com.mvpbrosproduction.simpleexpensetracker.composables.dialogs.EditExpenseCategoryDialog
import com.mvpbrosproduction.simpleexpensetracker.composables.ui.ExpenseCategoryIcon
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategory

@Composable
fun ExpenseCategoryListItem(
    expenseCategory: ExpenseCategory,
    favoriteToggled: () -> Unit,
    editCategory: (expenseCategory: ExpenseCategory) -> Unit,
    deleteCategory: (expenseCategory: ExpenseCategory) -> Unit
) {
    val favoriteIcon = if (expenseCategory.isFavorite) Icons.Sharp.Favorite else Icons.Sharp.FavoriteBorder
    val favoriteIconColor = Color(0Xfff63131)

    val deleteDialogOpen: MutableState<Boolean> = remember { mutableStateOf(false) }
    val editDialogOpen: MutableState<Boolean> = remember { mutableStateOf(false) }

    Row (modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically)
        {
            ExpenseCategoryIcon(
                category =  expenseCategory,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .size(40.dp)
                    .fillMaxHeight()
            )
            Text(text = expenseCategory.name.replaceFirstChar { it.titlecase() })
        }
        Row(modifier = Modifier.padding(horizontal = 8.dp), verticalAlignment = Alignment.CenterVertically)
        {
            // only show delete button for custom user defined categories
            if (expenseCategory.isCustom) {
                IconButton(
                    onClick = { deleteDialogOpen.value = true },
                    modifier = Modifier.then(Modifier.size(32.dp))
                ) {
                    Icon(Icons.Sharp.Delete, contentDescription = "Delete category")
                }
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            }
            // only show edit button for custom user defined categories
            if (expenseCategory.isCustom) {
                IconButton(
                    onClick = { editDialogOpen.value = true },
                    modifier = Modifier.then(Modifier.size(32.dp))
                ) {
                    Icon(Icons.Sharp.Edit, contentDescription = "Edit category")
                }
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            }
            IconButton(
                onClick = {
                    favoriteToggled()
                },
                modifier = Modifier.then(Modifier.size(32.dp)))
            {
                Icon(
                    favoriteIcon,
                    contentDescription = "Set category ${expenseCategory.name} as favourite",
                    tint = favoriteIconColor,
                )
            }
        }
    }
    
    if (deleteDialogOpen.value) {
        DeleteExpenseCategoryDialog(
            dialogOpen = deleteDialogOpen,
            category = expenseCategory,
            deleteCategory = deleteCategory
        )
    }

    if (editDialogOpen.value) {
        EditExpenseCategoryDialog(
            dialogOpen = editDialogOpen,
            category = expenseCategory,
            editCategory = editCategory
        )
    }
}