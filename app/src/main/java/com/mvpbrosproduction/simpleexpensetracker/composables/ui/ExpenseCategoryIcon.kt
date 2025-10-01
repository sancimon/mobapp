package com.mvpbrosproduction.simpleexpensetracker.composables.ui

import android.util.Log
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategory
import com.mvpbrosproduction.simpleexpensetracker.objects.DefaultExpenseCategories
import com.mvpbrosproduction.simpleexpensetracker.util.Util

private const val TAG = "ExpenseCategoryIcon"

@Composable
public fun ExpenseCategoryIcon(
    category: ExpenseCategory?,
    modifier: Modifier = Modifier
) {
    Log.d(TAG, category.toString())

    if (category == null) {
        val noCategoryFound = DefaultExpenseCategories.noCategoryFound

        Icon(
            painter = painterResource(id = noCategoryFound.iconId),
            contentDescription = "${noCategoryFound.name} category",
            tint = Color.Black,
            modifier = modifier
        )
    } else {
        val isDefaultCategory = DefaultExpenseCategories.map.keys.contains(category.name)

        if (isDefaultCategory) {
            // not-null assertion operator is ok since we tested if category name is default category
            val iconId = DefaultExpenseCategories.map[category.name]!!.iconId

            Icon(
                painter = painterResource(id = iconId),
                contentDescription = "${category.name} category",
                tint = Color.Black,
                modifier = modifier
            )
        } else {
            val tint: Float = category.tint!! // if custom category then it must contain tint

            val textColor: Color = Util.getExpenseCategoryIconTextColor(tint)

            CircleIcon(
                color = Color.hsv(tint, 1f, 1f),
                text = Util.extractInitials(category.name),
                textSize = 12.sp,
                textColor = textColor,
                modifier = modifier
            )
        }
    }
}

