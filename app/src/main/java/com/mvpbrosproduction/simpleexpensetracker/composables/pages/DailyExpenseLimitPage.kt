package com.mvpbrosproduction.simpleexpensetracker.composables.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.mvpbrosproduction.simpleexpensetracker.composables.dialogs.SetExpenseLimitDialog
import com.mvpbrosproduction.simpleexpensetracker.constants.DataStoreKeys
import com.mvpbrosproduction.simpleexpensetracker.ui.theme.SimpleExpenseTrackerTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking

private const val TAG = "DailyExpenseLimitPage"

@Composable
fun DailyExpenseLimitPage(dataStore: DataStore<Preferences>) {
    val currentExpenseLimitFlow = remember { dataStore.data.map { preferences ->
        preferences[DataStoreKeys.EXPENSE_LIMIT] ?: 0.0
    }}

    val currentExpenseLimit = runBlocking {
        currentExpenseLimitFlow.first()
    }

    val setExpenseLimitDialogOpen = remember { mutableStateOf(false) }

    val description = "This is where you can set your daily expense limit."
    val descriptionExtra = "It represents the amount of money you want to limit your daily spending to."

    SimpleExpenseTrackerTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            color = Color.White,
        ) {
            Column {
                Text(text = description)
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = descriptionExtra)
                Spacer(modifier = Modifier.height(16.dp))

                CurrentExpenseLimit(expenseLimit = currentExpenseLimit)
                Spacer(modifier = Modifier.height(16.dp))
                
                Button(onClick = { setExpenseLimitDialogOpen.value = true }, shape = RectangleShape) {
                    Text(text = "Set New Expense Limit")
                }
            }
        }
    }

    if (setExpenseLimitDialogOpen.value) {
        SetExpenseLimitDialog(
            currentExpenseLimit = currentExpenseLimit,
            onConfirm = { newExpenseLimit ->
                runBlocking {
                    dataStore.edit { variables ->
                        variables[DataStoreKeys.EXPENSE_LIMIT] = newExpenseLimit
                    }
                }
                setExpenseLimitDialogOpen.value = false
            },
            onClose = { setExpenseLimitDialogOpen.value = false }
        )
    }
}

@Composable
private fun CurrentExpenseLimit(expenseLimit: Double) {
    val annotatedString = buildAnnotatedString {
        append("Currently set to: ")
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(expenseLimit.toString())
        }
    }

    Text(
        text = annotatedString,
        fontSize = 20.sp
    )
}