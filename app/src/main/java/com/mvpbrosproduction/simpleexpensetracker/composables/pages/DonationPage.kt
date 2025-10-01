package com.mvpbrosproduction.simpleexpensetracker.composables.pages

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.mvpbrosproduction.simpleexpensetracker.ui.theme.SimpleExpenseTrackerTheme

private const val TAG = "DonationPage"

@Composable
fun DonationPage() {
    val clipboardManager = LocalClipboardManager.current
    val walletAddress = "0xc91B0919C11c44eE9b1750F4eE7fB79C5B5b5167"

    SimpleExpenseTrackerTheme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
                    .padding(32.dp),
            ) {
                Text(
                    text = "Hello there! :) Very briefly, the idea behind this app is not to provide an extensive tool for managing finances but instead an easy way to track small, day-to-day expenses that make you go \"Where did all the money go!?\" at the end of the month.",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "The app was designed to work locally so all the data lives on your phone.",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Text(
                    text = "If you find this app useful and would like to buy me a beer you can do so by sending some USDT to following address on BNB Smart Chain:",
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .border(1.dp, MaterialTheme.colorScheme.primary)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = walletAddress,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    IconButton(onClick = {
                        clipboardManager.setText(AnnotatedString(walletAddress))
                    }) {
                        Icon(
                            imageVector = Icons.Default.ContentCopy,
                            contentDescription = "copy wallet address",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}