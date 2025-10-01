package com.mvpbrosproduction.simpleexpensetracker

import android.content.ContentUris
import android.net.Uri
import android.provider.BaseColumns

object CategoryContract {
    internal const val TABLE_NAME = "Categories"

    val CONTENT_URI: Uri = Uri.withAppendedPath(CONTENT_AUTHORITY_URL, TABLE_NAME)

    const val CONTENT_TYPE = "vnd.android.cursor.dir/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"
    const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.$CONTENT_AUTHORITY.$TABLE_NAME"

    object Columns {
        const val ID = BaseColumns._ID
        const val NAME = "Name"
        const val DESCRIPTION = "Description"
        const val IS_FAVORITE = "Is_Favorite"
        const val IS_CUSTOM = "Is_Custom"
        const val TINT = "Tint"
    }

    fun getId(uri: Uri): Long {
        return  ContentUris.parseId(uri)
    }

    fun buildUriFromId(id: Long): Uri {
        return ContentUris.withAppendedId(ExpenseContract.CONTENT_URI, id)
    }
}