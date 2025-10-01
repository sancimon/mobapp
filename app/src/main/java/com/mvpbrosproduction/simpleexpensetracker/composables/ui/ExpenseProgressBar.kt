package com.mvpbrosproduction.simpleexpensetracker.composables.ui

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private const val TAG = "ExpenseProgressBar"

@Composable
fun ExpenseProgressBar(
    progress: Float = 0f,
    height: Int = 4,
    modifier: Modifier = Modifier,
) {
    LinearProgressIndicator(
        progress = progress,
        trackColor = Color.Green,
        color = Color.Red,
        modifier = modifier.fillMaxWidth().height(height.dp)
    )
}