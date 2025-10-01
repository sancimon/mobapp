package com.mvpbrosproduction.simpleexpensetracker.data_models

data class ExpenseCategoryCreate(
    val name: String,
    val description: String,
    val isFavorite: Boolean,
    val isCustom: Boolean,
    val tint: Float,
)