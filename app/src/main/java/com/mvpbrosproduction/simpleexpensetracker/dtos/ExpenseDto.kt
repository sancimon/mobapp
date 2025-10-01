package com.mvpbrosproduction.simpleexpensetracker.dtos

data class ExpenseDto(
    val id: Int? = null,
    val day: Int,
    val month: Int,
    val year: Int,
    val amount: Double,
    val categoryId: Int? = null,
)
