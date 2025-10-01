package com.mvpbrosproduction.simpleexpensetracker.composables

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Add
import androidx.compose.material.icons.sharp.Remove
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvpbrosproduction.simpleexpensetracker.util.ExpenseAmountConfig
import java.text.NumberFormat
import java.util.Locale

private const val TAG = "ExpenseInput"

@Composable
fun ExpenseInput(
    expenseAmountConfig: ExpenseAmountConfig,
    readonly: Boolean = true
) {
        Column (
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(horizontal = 80.dp)
        ) {
            if (!readonly) {
                Row (
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {
                            expenseAmountConfig.decreaseStep()
                        },
                        modifier = Modifier.size(24.dp).background(MaterialTheme.colorScheme.primary, shape = RectangleShape)
                    ) {
                        Icon(
                            Icons.Sharp.Remove,
                            "Remove digit",
                            modifier = Modifier.padding(0.dp),
                            tint = Color.White
                        )
                    }
                    Text(text = "DIGITS", style = TextStyle(fontSize = 24.sp))
                    IconButton(
                        onClick = {
                            expenseAmountConfig.increaseStep()
                        },
                        modifier = Modifier.size(24.dp).background(MaterialTheme.colorScheme.primary, shape = RectangleShape)
                    ) {
                        Icon(
                            Icons.Sharp.Add,
                            "Add digit",
                            modifier = Modifier.padding(0.dp),
                            tint = Color.White
                        )
                    }
                }
            }
            Row (
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                ExpenseAmountPicker(expenseAmountConfig)
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                if (expenseAmountConfig.multiplierState.value != 1) {
                    SubcomposeLayout { constraints ->
                        val multiplier: Int = expenseAmountConfig.multiplierState.value
                        val totalFormatted: String =
                            NumberFormat.getNumberInstance(Locale.ENGLISH).format(expenseAmountConfig.total.value)

                        val multiplierPlaceable = subcompose("text") {
                            Text(
                                text = "x ${String.format("%,d", multiplier)}",
                                fontSize = 16.sp,
                                color = Color.Gray,
                                textAlign = TextAlign.End
                            )
                        }[0].measure(constraints)

                        val totalPlaceable = subcompose("total") {
                            Text(
                                text = totalFormatted,
                                fontSize = 24.sp,
                                color = Color.DarkGray
                            )
                        }[0].measure(constraints)

                        val totalWidth = maxOf(totalPlaceable.width, multiplierPlaceable.width)

                        Log.d(TAG, totalWidth.toString())

                        val dividerPlaceable = subcompose("divider") {
                            Divider(
                                color = Color.Gray,
                                thickness = 1.dp,
                                modifier = Modifier.width(totalWidth.toDp()).padding(vertical = 8.dp)
                            )
                        }[0].measure(constraints)

                        val totalHeight = multiplierPlaceable.height + dividerPlaceable.height + totalPlaceable.height

                        layout(totalWidth, totalHeight) {
                            multiplierPlaceable.placeRelative(0, 0)
                            dividerPlaceable.placeRelative(0 , multiplierPlaceable.height)
                            totalPlaceable.placeRelative(0, multiplierPlaceable.height + dividerPlaceable.height)
                        }
                    }
                }
            }
        }
}