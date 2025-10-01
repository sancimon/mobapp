package com.mvpbrosproduction.simpleexpensetracker.repositories

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import com.mvpbrosproduction.simpleexpensetracker.CategoryContract
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategory
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategoryCreate

private const val TAG = "CategoryRepository"

class CategoryRepository constructor(private val context: Context) {
    public fun getCategoryByName(categoryName: String): ExpenseCategory? {
        val selection = CategoryContract.Columns.NAME + " = ?"

        val selectionArgs = arrayOf(categoryName)

        val cursor = context.contentResolver.query(
            CategoryContract.CONTENT_URI, null,selection, selectionArgs, null
        )

        cursor?.use {
            if (it.moveToFirst()) {
                return ExpenseCategory(
                    id = it.getInt(it.getColumnIndexOrThrow(CategoryContract.Columns.ID)),
                    name = it.getString(it.getColumnIndexOrThrow(CategoryContract.Columns.NAME)),
                    description = it.getString(it.getColumnIndexOrThrow(CategoryContract.Columns.DESCRIPTION)),
                    isCustom = it.getInt(it.getColumnIndexOrThrow(CategoryContract.Columns.IS_CUSTOM)) == 1,
                    isFavorite =  it.getInt(it.getColumnIndexOrThrow(CategoryContract.Columns.IS_FAVORITE)) == 1,
                    tint = it.getFloat(it.getColumnIndexOrThrow(CategoryContract.Columns.TINT))
                )
            }
        }
        return null
    }

    public fun addCategory(category: ExpenseCategoryCreate): Long? {

        val values = ContentValues().apply {
            put(CategoryContract.Columns.NAME, category.name)
            put(CategoryContract.Columns.DESCRIPTION, category.description)
            put(CategoryContract.Columns.IS_FAVORITE, category.isFavorite)
            put(CategoryContract.Columns.IS_CUSTOM, category.isCustom)
            put(CategoryContract.Columns.TINT, category.tint)
        }

        val uri: Uri? = context.contentResolver.insert(CategoryContract.CONTENT_URI, values)

        return uri?.let { CategoryContract.getId(it) }
    }

    public fun updateCategory(category: ExpenseCategory): Int {
        val selection = CategoryContract.Columns.ID + " = ?"

        val selectionArgs = arrayOf(category.id.toString())

        val values = ContentValues().apply {
            put(CategoryContract.Columns.NAME, category.name)
            put(CategoryContract.Columns.DESCRIPTION, category.description)
            put(CategoryContract.Columns.IS_FAVORITE, category.isFavorite)
            put(CategoryContract.Columns.IS_CUSTOM, category.isCustom)
            put(CategoryContract.Columns.TINT, category.tint)
        }

        return context.contentResolver.update(
            CategoryContract.CONTENT_URI, values, selection, selectionArgs
        )
    }

    public fun deleteCategory(category: ExpenseCategory): Int {
        val selection = CategoryContract.Columns.ID + " = ?"

        val selectionArgs = arrayOf(category.id.toString())

        return context.contentResolver.delete(
            CategoryContract.CONTENT_URI,
            selection,
            selectionArgs
        )
    }

    public fun loadCategories(): MutableList<ExpenseCategory> {
        val categories = mutableListOf<ExpenseCategory>()

        val cursor = context.contentResolver.query(CategoryContract.CONTENT_URI, null, null,null, null)

        cursor?.use {
            while (it.moveToNext()) {
                with(cursor) {
                    val id = getInt(it.getColumnIndexOrThrow(CategoryContract.Columns.ID))
                    val name = getString(it.getColumnIndexOrThrow(CategoryContract.Columns.NAME))
                    val description = getString(it.getColumnIndexOrThrow(CategoryContract.Columns.DESCRIPTION))
                    val isFavorite = getInt(it.getColumnIndexOrThrow(CategoryContract.Columns.IS_FAVORITE)) > 0
                    val isCustom = getInt(it.getColumnIndexOrThrow(CategoryContract.Columns.IS_CUSTOM)) > 0
                    val tint = getFloat(it.getColumnIndexOrThrow(CategoryContract.Columns.TINT))

                    val category = ExpenseCategory(
                        id,
                        name,
                        description,
                        isFavorite,
                        isCustom,
                        tint
                    )
                    categories.add(category)
                }
            }
        }
        return categories
    }
}