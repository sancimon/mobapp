package com.mvpbrosproduction.simpleexpensetracker

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns

object ExpenseContract {
    internal const val TABLE_NAME = "Expenses"

    val CONTENT_URI: Uri = Uri.withAppendedPath(CONTENT_AUTHORITY_URL, TABLE_NAME)

    const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"
    const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"

    object Columns {
        const val ID = BaseColumns._ID
        const val EXPENSE_DAY = "Day"
        const val EXPENSE_MONTH = "Month"
        const val EXPENSE_YEAR = "Year"
        const val EXPENSE_AMOUNT = "Amount"
        const val CATEGORY_ID = "Category_Id"
        const val DESCRIPTION = "Description"
    }

    fun getId(uri: Uri): Long {
        return  ContentUris.parseId(uri)
    }

    fun buildUriFromId(id: Long): Uri {
        return ContentUris.withAppendedId(CONTENT_URI, id)
    }
}