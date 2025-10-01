package com.mvpbrosproduction.simpleexpensetracker.composables

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvpbrosproduction.simpleexpensetracker.util.ExpenseAmountConfig

private const val TAG = "ExpenseAmountPicker"

@Composable
public fun ExpenseAmountPicker(
    expenseAmountConfig: ExpenseAmountConfig
) {
    val density = LocalDensity.current.run { 52.dp.toPx() }

    var rowWidth by remember { mutableIntStateOf(0) }
    var rowHeight by remember { mutableIntStateOf(0) }

    Box {
        Row (modifier = Modifier.onGloballyPositioned { layout ->
            rowWidth = layout.size.width
            rowHeight = layout.size.height
        }) {
            expenseAmountConfig.digits.forEachIndexed { index, _ ->
                NumberPicker(expenseAmountConfig.digits, index, 0, 9) {
                    expenseAmountConfig.calculateTotal()
                    Log.d(TAG, expenseAmountConfig.total.value.toString())
                }
            }
        }

        if (expenseAmountConfig.digitsState.value != expenseAmountConfig.maxNumberOfDigits) {
            Text(
                modifier = Modifier.offset { // Calculate offset based on the current index
                    val offsetX = (rowWidth / 4) * expenseAmountConfig.digitsState.value
                    val offsetY = (rowHeight / 2.5).toInt()
                    IntOffset(offsetX, offsetY)
                },
                fontSize = 28.sp,
                color = Color.Red,
                text = "."
            )
        }
    }
}