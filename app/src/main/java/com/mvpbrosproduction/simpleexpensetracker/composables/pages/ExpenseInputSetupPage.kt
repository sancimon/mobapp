package com.mvpbrosproduction.simpleexpensetracker.composables.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.mvpbrosproduction.simpleexpensetracker.composables.ExpenseInput
import com.mvpbrosproduction.simpleexpensetracker.ui.theme.SimpleExpenseTrackerTheme
import com.mvpbrosproduction.simpleexpensetracker.util.ExpenseAmountConfig

@Composable
public fun ExpenseInputSetupPage(dataStore: DataStore<Preferences>) {
    val expenseAmountConfig = ExpenseAmountConfig.getInstance(dataStore)

    SimpleExpenseTrackerTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
        ) {
            ExpenseInput(expenseAmountConfig = expenseAmountConfig, readonly = false)
        }
    }
}