package com.mvpbrosproduction.simpleexpensetracker

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri
import android.util.Log

private const val TAG = "AppProvider"

const val CONTENT_AUTHORITY = "com.mvpbrosproduction.simpleexpensetracker.provider"

private const val EXPENSES = 100
private const val EXPENSES_ID = 101

private const val CATEGORIES = 200
private const val CATEGORY_ID = 201

val CONTENT_AUTHORITY_URL: Uri = Uri.parse("content://$CONTENT_AUTHORITY")

class AppProvider: ContentProvider() {

    private val uriMatcher by lazy { buildUriMatcher() }

    private fun buildUriMatcher(): UriMatcher {
        Log.d(TAG, "buildUriMatcher starts")
        val matcher = UriMatcher(UriMatcher.NO_MATCH)

        matcher.addURI(CONTENT_AUTHORITY, ExpenseContract.TABLE_NAME, EXPENSES)

        matcher.addURI(CONTENT_AUTHORITY, "${ExpenseContract.TABLE_NAME}/#", EXPENSES_ID)

        matcher.addURI(CONTENT_AUTHORITY, CategoryContract.TABLE_NAME, CATEGORIES)

        matcher.addURI(CONTENT_AUTHORITY, "${CategoryContract.TABLE_NAME}/#", CATEGORY_ID)

        return matcher
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        Log.d(TAG, "query: called with uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "query: match is $match")

        val queryBuilder = SQLiteQueryBuilder()

        when(match) {
            EXPENSES -> queryBuilder.tables = ExpenseContract.TABLE_NAME

            EXPENSES_ID -> {
                queryBuilder.tables = ExpenseContract.TABLE_NAME
                val expenseId = ExpenseContract.getId(uri)
                queryBuilder.appendWhereEscapeString("${ExpenseContract.Columns.ID} = $expenseId")
            }

            CATEGORIES -> queryBuilder.tables = CategoryContract.TABLE_NAME

            CATEGORY_ID -> {
                queryBuilder.tables = CategoryContract.TABLE_NAME
                val categoryId = CategoryContract.getId(uri)
                queryBuilder.appendWhereEscapeString("${CategoryContract.Columns.ID} = $categoryId")
            }

            else -> throw IllegalArgumentException("Unknown uri: $uri")
        }

        val db = AppDatabase.getInstance(context!!).readableDatabase
        val cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder)

        Log.d(TAG, "query rows in returned cursor = ${cursor.count}")

        return cursor
    }

    override fun getType(uri: Uri): String? {
        val match = uriMatcher.match(uri)

        return when (match) {
            EXPENSES -> ExpenseContract.CONTENT_TYPE

            EXPENSES_ID -> ExpenseContract.CONTENT_ITEM_TYPE

            CATEGORIES -> CategoryContract.CONTENT_TYPE

            CATEGORY_ID -> CategoryContract.CONTENT_ITEM_TYPE

            else -> throw IllegalArgumentException("unknown uri: $uri")
        }
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val match = uriMatcher.match(uri)
        val recordId: Long
        val returnUri: Uri

        when (match) {
            EXPENSES -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                recordId = db.insert(ExpenseContract.TABLE_NAME, null, values)

                if (recordId != -1L) {
                    returnUri = ExpenseContract.buildUriFromId(recordId)
                } else {
                    throw SQLException("Failed to insert")
                }
            }
            CATEGORIES -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                recordId = db.insert(CategoryContract.TABLE_NAME, null, values)

                if (recordId != -1L) {
                    returnUri = CategoryContract.buildUriFromId(recordId)
                } else {
                    throw SQLException("Failed to insert")
                }
            }
            else -> throw IllegalArgumentException("unknown uri: $uri")
        }
        return returnUri
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        Log.d(TAG, "delete: called with uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "delete: match is $match")

        val affectedRows: Int = when (match) {
            EXPENSES -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                db.delete(ExpenseContract.TABLE_NAME, selection, selectionArgs)
            }

            CATEGORIES -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                db.delete(CategoryContract.TABLE_NAME, selection, selectionArgs)
            }

            else -> throw  IllegalArgumentException("unknown uri: $uri")
        }
        return affectedRows
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        Log.d(TAG, "update: called with uri $uri")
        val match = uriMatcher.match(uri)
        Log.d(TAG, "update: match is $match")

        val count: Int
        var selectionCriteria: String

        when (match) {
            EXPENSES -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                count = db.update(ExpenseContract.TABLE_NAME, values, selection, selectionArgs)
            }
            EXPENSES_ID -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                val id = ExpenseContract.getId(uri)
                selectionCriteria = "${ExpenseContract.Columns.ID} = $id"

                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += " AND ($selection)"
                }
                count = db.update(ExpenseContract.TABLE_NAME, values, selectionCriteria, selectionArgs)
            }
            CATEGORIES -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                count = db.update(CategoryContract.TABLE_NAME, values, selection, selectionArgs)
            }
            CATEGORY_ID -> {
                val db = AppDatabase.getInstance(context!!).writableDatabase
                val id = ExpenseContract.getId(uri)
                selectionCriteria = "${CategoryContract.Columns.ID} = $id"

                if (selection != null && selection.isNotEmpty()) {
                    selectionCriteria += " AND ($selection)"
                }
                count = db.update(CategoryContract.TABLE_NAME, values, selectionCriteria, selectionArgs)
            }
            else -> throw IllegalArgumentException("unknown uri: $uri")
        }
        Log.d(TAG, "Exiting update, returning $count")
        return count
    }
}