package com.mvpbrosproduction.simpleexpensetracker.composables.inputs

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private const val TAG = "NumberInput"

@Composable
fun NumberInput(
    modifier: Modifier = Modifier,
    initValue: Double,
    onValueChanged: (Double) -> Unit
) {
    val (value, setValue) = remember { mutableStateOf(initValue.toString()) }

    BasicTextField(
        value = value,
        onValueChange = { inputValue ->
            Log.d(TAG, inputValue);
            
            // we reassign it to var so it can be reset to 0 when empty
            var updatedInputValue = inputValue

            val dotCount = updatedInputValue.count { char -> char == '.'}

            if (dotCount > 1) {
                return@BasicTextField
            }

            if (updatedInputValue.isEmpty()) {
                updatedInputValue = "0"
            }

            // Ensure that only numbers are typed
            if (updatedInputValue.all { char -> char.isDigit() || char == '.' }) {
                setValue(updatedInputValue)
                // TODO: Try make this more robust
                onValueChanged(updatedInputValue.toDouble())
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .background(Color.White)
                    .border(1.dp, Color.Gray)
                    .padding(8.dp, 0.dp)
                    .height(36.dp)
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    if (value.isEmpty()) {
                        Text("Enter a number", style = TextStyle(color = Color.Gray))
                    }
                    innerTextField()
                }
            }
        },
        textStyle = TextStyle(fontSize = 16.sp),
        modifier = modifier,
    )
}