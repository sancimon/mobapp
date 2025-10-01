package com.mvpbrosproduction.simpleexpensetracker.composables.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.mvpbrosproduction.simpleexpensetracker.ui.theme.SimpleExpenseTrackerTheme

@Composable
fun YearlyExpenses(onBackBtnClick: () -> Unit) {
    SimpleExpenseTrackerTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.padding(top = 32.dp, bottom = 32.dp)
            ) {
                Column(
                    modifier = Modifier.fillMaxHeight(0.9f)
                ) {
                    Text(
                        text = "Coming soon...",
                        modifier = Modifier
                            .padding(top = 16.dp)
                            .align(alignment = Alignment.CenterHorizontally)
                    )
                }
            }
        }
    }
}