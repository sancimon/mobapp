package com.mvpbrosproduction.simpleexpensetracker

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.mvpbrosproduction.simpleexpensetracker.objects.DefaultExpenseCategories

private const val TAG = "AppDatabase"
private const val DATABASE_NAME = "ExpenseTracker.db"
private const val DATABASE_VERSION = 1

internal class AppDatabase constructor(context: Context): SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    init {
        Log.d(TAG, "AppDatabase: initializing")
    }

    override fun onCreate(db: SQLiteDatabase?) {
        Log.d(TAG, "onCreate: starts")

        val categoryTableSQL = """CREATE TABLE ${CategoryContract.TABLE_NAME} (
            ${CategoryContract.Columns.ID} INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
            ${CategoryContract.Columns.NAME} TEXT NOT NULL,
            ${CategoryContract.Columns.DESCRIPTION} TEXT NOT NULL,
            ${CategoryContract.Columns.IS_FAVORITE} BOOL NOT NULL,
            ${CategoryContract.Columns.IS_CUSTOM} BOOL NOT NULL,
            ${CategoryContract.Columns.TINT} FLOAT);
        """.replaceIndent(" ")

        Log.d(TAG, "Create category table: $categoryTableSQL")

        db?.execSQL(categoryTableSQL)

        val expenseTableSQL = """CREATE TABLE ${ExpenseContract.TABLE_NAME} (
            ${ExpenseContract.Columns.ID} INTEGER PRIMARY KEY NOT NULL,
            ${ExpenseContract.Columns.EXPENSE_DAY} INTEGER NOT NULL,
            ${ExpenseContract.Columns.EXPENSE_MONTH} INTEGER NOT NULL,
            ${ExpenseContract.Columns.EXPENSE_YEAR} INTEGER NOT NULL,
            ${ExpenseContract.Columns.EXPENSE_AMOUNT} REAL NOT NULL,
            ${ExpenseContract.Columns.DESCRIPTION} TEXT,
            ${ExpenseContract.Columns.CATEGORY_ID} INTEGER,
            FOREIGN KEY (${ExpenseContract.Columns.CATEGORY_ID}) REFERENCES ${CategoryContract.TABLE_NAME}(${CategoryContract.Columns.ID}) ON UPDATE CASCADE
        );
        """.replaceIndent(" ")

        Log.d(TAG, "Create expense table: $expenseTableSQL")

        db?.execSQL(expenseTableSQL)

        _insertDefaultExpenseCategoriesIntoDb(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        Log.d(TAG, "onUpgrade: starts")

        when (oldVersion) {
            1 -> {
                // upgrade logic from version 1
            }
            else -> throw IllegalStateException("onUpgrade with unknown new version: $newVersion")
        }
    }

    private fun _insertDefaultExpenseCategoriesIntoDb(db: SQLiteDatabase?) {
        Log.d(TAG, "_insertDefaultExpenseCategoriesIntoDb: starts")

        DefaultExpenseCategories.map.values.forEach() { defaultCategory ->
            val insertSql = """INSERT INTO ${CategoryContract.TABLE_NAME} (
                ${CategoryContract.Columns.NAME},
                ${CategoryContract.Columns.DESCRIPTION},
                ${CategoryContract.Columns.IS_CUSTOM},
                ${CategoryContract.Columns.IS_FAVORITE}) 
                VALUES ('${defaultCategory.name}','${defaultCategory.description}',FALSE,${defaultCategory.isFavorite});
            """.replaceIndent(" ")

            Log.d(TAG, "Insert expense category: $insertSql")

            db?.execSQL(insertSql)
        }
    }

    companion object: SingletonHolder<AppDatabase, Context>(::AppDatabase)
}