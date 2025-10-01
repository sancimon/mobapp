package com.mvpbrosproduction.simpleexpensetracker.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvpbrosproduction.simpleexpensetracker.composables.inputs.ExpenseCategoryDropdown
import com.mvpbrosproduction.simpleexpensetracker.composables.ui.ExpenseCategoryIcon
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategory

private val TAG = "ExpenseCategoryPicker"

@Composable
fun ExpenseCategoryPicker(
    favoriteCategories: List<ExpenseCategory>,
    nonFavoriteCategories: List<ExpenseCategory>,
    onCategorySelected: (ExpenseCategory) -> Unit
) {
    val selectedExpenseCategory = remember { mutableStateOf(favoriteCategories.first()) }

    val selectedNonFavoriteExpenseCategory = remember {
        derivedStateOf {
            if (nonFavoriteCategories.any { it.id == selectedExpenseCategory.value.id }) {
                Log.d(TAG, "Non favourite selected: ${selectedExpenseCategory.value}")
                selectedExpenseCategory.value
            } else {
                Log.d(TAG, "Non favourite deselected: ${selectedExpenseCategory.value}")
                null
            }
        }
    }

    Column {
        Text(
            text = "Choose expense category:", fontSize = 16.sp,
            fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 8.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .background(Color.White)
                .heightIn(max = 300.dp)
                .padding(bottom = 24.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            favoriteCategories.forEach { expenseCategory ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        ExpenseCategoryIcon(
                            category = expenseCategory,
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(32.dp)
                        )
                        Text(
                            text = expenseCategory.name.replaceFirstChar { it.titlecase() },
                            color = Color.Black
                        )
                    }
                    RadioButton(
                        selected = expenseCategory.name == selectedExpenseCategory.value.name,
                        onClick = {
                            // Update UI for selected category
                            selectedExpenseCategory.value = expenseCategory

                            // Notify parent component on selection
                            onCategorySelected(expenseCategory)
                        },
                        modifier = Modifier.semantics {
                            contentDescription = "Select category ${expenseCategory.name}"
                        }
                    )
                }
            }
        }


    }
}