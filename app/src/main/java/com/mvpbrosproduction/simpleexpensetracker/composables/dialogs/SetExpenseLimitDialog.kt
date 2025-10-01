package com.mvpbrosproduction.simpleexpensetracker.composables.dialogs

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mvpbrosproduction.simpleexpensetracker.composables.inputs.NumberInput

@Composable
fun SetExpenseLimitDialog(
    currentExpenseLimit: Double,
    onClose: () -> Unit,
    onConfirm: (expenseLimit: Double) -> Unit
) {

    var newExpenseLimit = 0.0;

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.3f))
    ) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = onClose,
            confirmButton = {
                TextButton(onClick = {
                    onConfirm(newExpenseLimit)
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
                Column {
                    Text(text = "New Limit")

                    Spacer(modifier = Modifier.height(16.dp))

                    NumberInput(initValue = currentExpenseLimit) {
                        newExpenseLimit = it
                    }
                }
            }
        )
    }
}
