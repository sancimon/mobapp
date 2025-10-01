package com.mvpbrosproduction.simpleexpensetracker.data_models

data class ExpenseCategory(
    val id: Int,
    val name: String,
    val description: String,
    val isFavorite: Boolean,
    val isCustom: Boolean,
    val tint: Float? = null,
) {
    override fun toString(): String {
        return "$name"
    }
}
