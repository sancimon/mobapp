package com.mvpbrosproduction.simpleexpensetracker.data_models

import com.mvpbrosproduction.simpleexpensetracker.dtos.ExpenseDto

data class Expense(
    val id: Int? = null,
    val day: Int,
    val month: Int,
    val year: Int,
    val amount: Double,
    val category: ExpenseCategory?,
    val description: String? = null
) {
    public override fun toString(): String {
        return "id=$id,amount=$amount,date=$day/$month/$year,category=($category)"
    }

    public fun toDto(): ExpenseDto {
        return ExpenseDto(id, day, month, year, amount, category?.id)
    }
}