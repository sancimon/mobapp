package com.mvpbrosproduction.simpleexpensetracker.composables.pages

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvpbrosproduction.simpleexpensetracker.composables.dialogs.CreateExpenseCategoryDialog
import com.mvpbrosproduction.simpleexpensetracker.composables.list_entries.ExpenseCategoryListItem
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategoryCreate
import com.mvpbrosproduction.simpleexpensetracker.managers.ExpenseCategoryManager
import com.mvpbrosproduction.simpleexpensetracker.ui.theme.SimpleExpenseTrackerTheme
import kotlinx.coroutines.launch

@Composable
fun CategoriesSetupPage(categoryManager: ExpenseCategoryManager) {
    val createDialogOpen = remember {
        mutableStateOf(false)
    }

    val coroutineScope = rememberCoroutineScope()

    val onCategoryCreated: (ExpenseCategoryCreate) -> Unit = { category ->
        categoryManager.createCategory(category)

        createDialogOpen.value = false
    }

    SimpleExpenseTrackerTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize().padding(top = 20.dp),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 24.dp, vertical = 32.dp)
            ) {
                LazyColumn(
                    modifier = Modifier.run {
                        border(width = 1.dp, color = MaterialTheme.colorScheme.primary)
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                            .fillMaxHeight(0.85f)

                    }
                ) {
                    items(categoryManager.categories.value) { expenseCategory  ->
                        ExpenseCategoryListItem(
                            expenseCategory = expenseCategory,
                            favoriteToggled = {
                                categoryManager.toggleCategoryFavorite(expenseCategory)
                            },
                            editCategory = { category ->
                                categoryManager.editCategory(category)
                            },
                            deleteCategory = { category ->
                                coroutineScope.launch {
                                    categoryManager.deleteCategory(category)
                                }
                            }
                        )
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
                        createDialogOpen.value = true
                    },
                    shape = RectangleShape,
                    modifier = Modifier
                        .wrapContentHeight()
                        .width(182.dp)
                ) {
                    Text(text = "CREATE")
                }
            }
        }
    }
    
    if (createDialogOpen.value) {
        CreateExpenseCategoryDialog(
            dialogOpen = createDialogOpen,
            createCategory = onCategoryCreated
        )
    }
}