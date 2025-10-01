package com.mvpbrosproduction.simpleexpensetracker.models

import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val label: String,
    val icon: ImageVector,
    val target: String
) {
    override fun toString(): String {
        return "Navigation Label: $label"
    }
}