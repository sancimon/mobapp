package com.mvpbrosproduction.simpleexpensetracker.composables.inputs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    modifier: Modifier? = null
) {
    TextField(
        value = searchText,
        onValueChange = onSearchTextChange,
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search") },
        label = { Text("Search") },
        modifier = modifier ?: Modifier
    )
}
