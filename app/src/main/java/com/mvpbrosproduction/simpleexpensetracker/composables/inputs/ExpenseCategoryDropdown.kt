package com.mvpbrosproduction.simpleexpensetracker.composables.inputs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.mvpbrosproduction.simpleexpensetracker.composables.ui.ExpenseCategoryIcon
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategory
import com.mvpbrosproduction.simpleexpensetracker.managers.ExpenseCategoryManager

private val TAG = "ExpenseCategoryDropdown"

@Composable
fun ExpenseCategoryDropdown(
    modifier: Modifier,
    categoryManager: ExpenseCategoryManager,
    value: ExpenseCategory? = null,
    size: Dp = 54.dp,
    onCategorySelected: (ExpenseCategory) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    var selectedCategory by remember { mutableStateOf(value) }

    val expenseCategories =
        categoryManager.categories.value.sortedWith(
            compareByDescending<ExpenseCategory> { it.isFavorite }.thenBy { it.name })

    var buttonWidth by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Box(contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedButton(
                onClick = { expanded = true },
                shape = RectangleShape,
                modifier = modifier
                    .height(size)
                    .onGloballyPositioned { layoutCoordinates ->
                        buttonWidth = with(density) { layoutCoordinates.size.width.toDp() }
                    },
                contentPadding = PaddingValues(8.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp, 0.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (selectedCategory !== null) {
                        ExpenseCategoryIcon(
                            selectedCategory
                        )
                        Text(
                            text = (selectedCategory?.name
                                ?: "").replaceFirstChar { it.uppercase() },
                            color = Color.Black,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal
                        )
                        Icon(
                            imageVector = Icons.Sharp.KeyboardArrowDown,
                            contentDescription = "Arrow down icon",
                            tint = Color.Black
                        )
                    } else {
                        Text(text = "Select category...", color = Color.DarkGray, fontSize = 16.sp)

                        Icon(
                            imageVector = Icons.Sharp.KeyboardArrowDown,
                            contentDescription = "Arrow down icon",
                            tint = Color.Black
                        )
                    }
                }
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = modifier
                .width(buttonWidth)
                .zIndex(1f)
                .heightIn(max = 240.dp, min = 120.dp)
        ) {
            expenseCategories.forEach { expenseCategory ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false

                        selectedCategory = expenseCategory

                        onCategorySelected(expenseCategory)
                    },
                    text = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp, 0.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ExpenseCategoryIcon(
                                expenseCategory
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = expenseCategory.name.replaceFirstChar { it.uppercase() },
                                color = Color.Black,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                )
            }
        }
    }
}