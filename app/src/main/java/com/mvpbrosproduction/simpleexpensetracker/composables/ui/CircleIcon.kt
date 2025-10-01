package com.mvpbrosproduction.simpleexpensetracker.composables.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun CircleIcon(
    color: Color,
    text: String? = null,
    textSize: TextUnit? = null,
    textColor: Color? = null,
    fontStyle: FontStyle = FontStyle.Normal,
    modifier: Modifier = Modifier
) {

    Box (modifier = Modifier.padding(0.dp)) {
        Icon(
            imageVector = Icons.Default.Circle,
            contentDescription = null,
            tint = color,
            modifier = modifier
        )
        if (text != null && textSize != null) {
            Text(
                text = text,
                modifier = Modifier.align(Alignment.Center),
                // Optionally style the text:
                fontSize = textSize,
                fontStyle = fontStyle,
                fontWeight = FontWeight.Bold,
                color = textColor ?: Color.Black
            )
        }
    }
}