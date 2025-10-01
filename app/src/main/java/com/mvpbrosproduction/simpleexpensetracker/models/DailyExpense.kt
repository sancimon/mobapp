package com.mvpbrosproduction.simpleexpensetracker.models

import com.mvpbrosproduction.simpleexpensetracker.data_models.Expense
import com.mvpbrosproduction.simpleexpensetracker.enums.DailyExpenseStatus

class DailyExpense constructor(
    public val date: SimpleDate,
    public val list: List<Expense>
) {
    public var status: DailyExpenseStatus = DailyExpenseStatus.NOT_EXCEEDED

    public var allowedSpending: Double = 0.0

    public val total: Double
        get() = list.sumOf { expense -> expense.amount }

    public fun addExpense(expense: Expense): DailyExpense {
        return DailyExpense(date, list + expense)
    }
}