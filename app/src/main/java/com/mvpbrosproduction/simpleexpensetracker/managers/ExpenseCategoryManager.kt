package com.mvpbrosproduction.simpleexpensetracker.managers

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategory
import com.mvpbrosproduction.simpleexpensetracker.data_models.ExpenseCategoryCreate
import com.mvpbrosproduction.simpleexpensetracker.repositories.CategoryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

private const val TAG = "ExpenseCategoryManager"

class ExpenseCategoryManager constructor(private val _categoryRepository: CategoryRepository) {
    public val categories: MutableState<List<ExpenseCategory>>

    init {
        val allCategories: List<ExpenseCategory> = _categoryRepository.loadCategories()

        val sortedCategories = allCategories.sortedWith(
            compareByDescending<ExpenseCategory> { it.isFavorite }
                .thenBy { it.name }
        )

        categories = mutableStateOf(sortedCategories)
    }

    public fun createCategory(category: ExpenseCategoryCreate): Boolean {
        val id: Long? = _categoryRepository.addCategory(category)

        _reloadCategories()

        return id != null
    }

    public fun editCategory(category: ExpenseCategory): Boolean {
        val numOfUpdatedRows: Int = _categoryRepository.updateCategory(category)

        _reloadCategories()

        return numOfUpdatedRows > 0
    }

    public suspend fun deleteCategory(category: ExpenseCategory): Boolean = withContext(Dispatchers.IO) {
        val numOfDeletedRows = _categoryRepository.deleteCategory(category)

        if (numOfDeletedRows > 0) {
            categories.value = categories.value.filter { c -> c.id != category.id }
        }

        numOfDeletedRows > 0
    }

    public fun findCategoryByName(categoryName: String): ExpenseCategory? {
        return _categoryRepository.getCategoryByName(categoryName)
    }

    public fun toggleCategoryFavorite(category: ExpenseCategory) {
        val updatedCategory: ExpenseCategory = category.copy(isFavorite = !category.isFavorite)

        _categoryRepository.updateCategory(updatedCategory)

        _reloadCategories()
    }

    private fun _reloadCategories() {
        val allCategories: List<ExpenseCategory> = _categoryRepository.loadCategories()

        val sortedCategories = allCategories.sortedWith(
            compareByDescending<ExpenseCategory> { it.isFavorite }
                .thenBy { it.name.lowercase() }
        )

        categories.value = sortedCategories
    }
}