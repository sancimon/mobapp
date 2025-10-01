package com.mvpbrosproduction.simpleexpensetracker.repositories

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.util.Log
import com.mvpbrosproduction.simpleexpensetracker.CategoryContract
import com.mvpbrosproduction.simpleexpensetracker.ExpenseContract
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategory
import com.mvpbrosproduction.simpleexpensetracker.data_models.Expense
import com.mvpbrosproduction.simpleexpensetracker.dtos.ExpenseDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "ExpenseRepository"

class ExpenseRepository constructor(private val context: Context) {

    public suspend fun addExpense(expense: ExpenseDto): Long {
        return withContext(Dispatchers.IO) {
            val values = ContentValues().apply {
                put(ExpenseContract.Columns.EXPENSE_DAY, expense.day)
                put(ExpenseContract.Columns.EXPENSE_MONTH, expense.month)
                put(ExpenseContract.Columns.EXPENSE_YEAR, expense.year)
                put(ExpenseContract.Columns.EXPENSE_AMOUNT, expense.amount)
                put(ExpenseContract.Columns.CATEGORY_ID, expense.categoryId)
            }

            val uri: Uri? = context.contentResolver.insert(ExpenseContract.CONTENT_URI, values)

            var index: Long = -1L

            if (uri != null) {
                index = ExpenseContract.getId(uri)

                Log.d(TAG, "Expense added to db. expense=$expense")
            } else {
                Log.d(TAG, "Failed to add expense to db. expense=$expense")
            }
            index
        }
    }

    public fun deleteExpense(expense: Expense): Int {
        val selection = ExpenseContract.Columns.ID + " = ?"

        val selectionArgs = arrayOf(expense.id.toString())

        val numOfDeletedRows: Int = context.contentResolver.delete(
            ExpenseContract.CONTENT_URI, selection, selectionArgs
        )

        if (numOfDeletedRows > 1) {
            Log.d(TAG, "Expense deleted from db. expense=$expense")
        } else {
            Log.d(TAG, "Failed to delete expense from db. expense=$expense")
        }
        return numOfDeletedRows
    }

    public fun updateExpense(expense: Expense): Int {
        val selection = ExpenseContract.Columns.ID + " = ?"

        val selectionArgs = arrayOf(expense.id.toString())

        val values = ContentValues().apply {
            put(ExpenseContract.Columns.EXPENSE_AMOUNT, expense.amount)
            put(ExpenseContract.Columns.CATEGORY_ID, expense.category?.id)
            put(ExpenseContract.Columns.DESCRIPTION, expense.description)
        }

        return context.contentResolver.update(
            ExpenseContract.CONTENT_URI, values, selection, selectionArgs
        )
    }

    public fun loadDailyExpenses(day: Int, month: Int, year: Int): List<Expense> {
        val expenses = mutableListOf<Expense>()

        val selection =
            ExpenseContract.Columns.EXPENSE_DAY + " = ? AND " +
            ExpenseContract.Columns.EXPENSE_MONTH + " = ? AND " +
            ExpenseContract.Columns.EXPENSE_YEAR + " = ?"

        val selectionArgs = arrayOf(day.toString(), month.toString(), year.toString())

        val sortOrder = "${ExpenseContract.Columns.ID} DESC"

        val cursor = context.contentResolver.query(
            ExpenseContract.CONTENT_URI,
            null,
            selection,
            selectionArgs,
            sortOrder)

        cursor?.use {
            while (it.moveToNext()) {
                with (cursor) {
                    val id = getInt(it.getColumnIndexOrThrow(ExpenseContract.Columns.ID))
                    val day = getInt(it.getColumnIndexOrThrow(ExpenseContract.Columns.EXPENSE_DAY))
                    val month = getInt(it.getColumnIndexOrThrow(ExpenseContract.Columns.EXPENSE_MONTH))
                    val year = getInt(it.getColumnIndexOrThrow(ExpenseContract.Columns.EXPENSE_YEAR))
                    val amount = getDouble(it.getColumnIndexOrThrow(ExpenseContract.Columns.EXPENSE_AMOUNT))
                    val categoryId = getInt(it.getColumnIndexOrThrow(ExpenseContract.Columns.CATEGORY_ID))
                    val description = getString(it.getColumnIndexOrThrow(ExpenseContract.Columns.DESCRIPTION))

                    val category: ExpenseCategory? = _getCategoryById(categoryId)

                    val expense = Expense(id, day, month, year, amount, category, description)

                    expenses.add(expense)
                }
            }
        }
        return expenses
    }

    public fun loadMonthlyExpenses(month: Int, year: Int): List<Expense> {

        val monthlyExpenses = mutableListOf<Expense>()

        val selection =
            ExpenseContract.Columns.EXPENSE_MONTH + " = ? AND " +
            ExpenseContract.Columns.EXPENSE_YEAR + " = ?"

        val selectionArgs = arrayOf(month.toString(), year.toString())

        val sortOrder = "${ExpenseContract.Columns.ID} ASC"

        val cursor = context.contentResolver.query(
            ExpenseContract.CONTENT_URI,
            null,
            selection,
            selectionArgs,
            sortOrder
        )

        cursor?.use {
            while (it.moveToNext()) {
                with (cursor) {
                    val id = getInt(it.getColumnIndexOrThrow(ExpenseContract.Columns.ID))
                    val day = getInt(it.getColumnIndexOrThrow(ExpenseContract.Columns.EXPENSE_DAY))
                    val month = getInt(it.getColumnIndexOrThrow(ExpenseContract.Columns.EXPENSE_MONTH))
                    val year = getInt(it.getColumnIndexOrThrow(ExpenseContract.Columns.EXPENSE_YEAR))
                    val amount = getDouble(it.getColumnIndexOrThrow(ExpenseContract.Columns.EXPENSE_AMOUNT))
                    val categoryId = getInt(it.getColumnIndexOrThrow(ExpenseContract.Columns.CATEGORY_ID))
                    val description = getString(it.getColumnIndexOrThrow(ExpenseContract.Columns.DESCRIPTION))

                    val category: ExpenseCategory? = _getCategoryById(categoryId)

                    val expense = Expense(id, day, month, year, amount, category, description)

                    monthlyExpenses.add(expense)
                }
            }
        }
        return monthlyExpenses
    }

    private fun _getCategoryById(categoryId: Int): ExpenseCategory? {
        var category: ExpenseCategory? = null

        val categoryCursor = context.contentResolver.query(
            CategoryContract.CONTENT_URI,
            null,
            "${CategoryContract.Columns.ID} = ?",
            arrayOf(categoryId.toString()),
            null
        )

        categoryCursor?.use {
            with(categoryCursor) {
                if (it.moveToFirst()) {
                    val cId = getInt(it.getColumnIndexOrThrow(CategoryContract.Columns.ID))
                    val cName = getString(it.getColumnIndexOrThrow(CategoryContract.Columns.NAME))
                    val cDesc = getString(it.getColumnIndexOrThrow(CategoryContract.Columns.DESCRIPTION))
                    val isFavorite = getInt(it.getColumnIndexOrThrow(CategoryContract.Columns.IS_FAVORITE)) > 0
                    val isCustom = getInt(it.getColumnIndexOrThrow(CategoryContract.Columns.IS_FAVORITE)) > 0
                    val tint = getFloat(it.getColumnIndexOrThrow(CategoryContract.Columns.TINT))

                    category = ExpenseCategory(
                        cId,
                        cName,
                        cDesc,
                        isFavorite,
                        isCustom,
                        tint
                    )
                }
            }
        }
        return category
    }
}