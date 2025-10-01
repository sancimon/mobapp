package com.mvpbrosproduction.simpleexpensetracker.composables.dialogs

import HueBar
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mvpbrosproduction.simpleexpensetracker.composables.ui.CircleIcon
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategoryCreate
import com.mvpbrosproduction.simpleexpensetracker.util.Util

private const val TAG = "CreateExpenseCategoryDialog"

@Composable
public fun CreateExpenseCategoryDialog(
    dialogOpen: MutableState<Boolean>,
    createCategory: (category: ExpenseCategoryCreate) -> Unit,
) {
    val name = remember {
        mutableStateOf("")
    }

    val description = remember {
        mutableStateOf("")
    }

    val initials = remember {
        mutableStateOf("")
    }

    val hue = remember {
        mutableFloatStateOf(0f)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        AlertDialog(
            containerColor = Color.White,
            onDismissRequest = {
                dialogOpen.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        val category = ExpenseCategoryCreate(
                            name = name.value,
                            description = description.value,
                            isFavorite = true,
                            isCustom = true,
                            tint = hue.floatValue
                        )
                        createCategory(category)
                    },
                    enabled = name.value.isNotEmpty()
                ) {
                    Text(text = "OK")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    dialogOpen.value = false
                }) {
                    Text(text = "CANCEL")
                }
            },
            text = {
                val textColor = Util.getExpenseCategoryIconTextColor(hue.floatValue)

                Column {
                    Text(
                        text = "Create category",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(bottom = 20.dp)
                    )
                    Text(text = "Name:", modifier = Modifier.padding(bottom = 8.dp))
                    TextField(
                        value = name.value,
                        onValueChange = { text ->
                            name.value = text
                            initials.value = Util.extractInitials(text)
                        },
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .height(48.dp)
                    )
                    Text(text = "Description:", modifier = Modifier.padding(bottom = 8.dp))
                    TextField(
                        value = description.value,
                        onValueChange = { text ->
                            description.value = text
                        },
                        minLines = 2,
                        maxLines = 3,
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                    )
                    Text(text = "Color:", modifier = Modifier.padding(bottom = 8.dp))
                    HueBar(setColor = { selectedHue ->
                        hue.floatValue = selectedHue
                    })
                    Text(
                        text = "Appearance:",
                        modifier = Modifier.padding(top = 16.dp, bottom = 8.dp)
                    )
                    CircleIcon(
                        color = Color.hsv(hue.floatValue, 1f, 1f),
                        text = initials.value,
                        textSize = 12.sp,
                        textColor = textColor,
                        modifier = Modifier.size(32.dp)
                    )
                }
            },
        )
    }
}
