package com.mvpbrosproduction.simpleexpensetracker.models

import com.mvpbrosproduction.simpleexpensetracker.data_models.Expense
import com.mvpbrosproduction.simpleexpensetracker.enums.DailyExpenseStatus
import java.time.YearMonth
import kotlin.math.abs

private const val TAG = "MonthlyExpense"

class MonthlyExpense constructor(
    public val date: SimpleDate,
    public val list: List<Expense>,
    private val _dailyExpenseLimit: Double
) {
    public val dailyExpenses: List<DailyExpense>

    init {
        val dailyExpensesByDate = _sortedExpensesByDate(list)

        _calculateDailyExpenseStatusesAndAllowedSpending(dailyExpensesByDate)

        dailyExpenses = dailyExpensesByDate
    }

    private fun _sortedExpensesByDate(expenses: List<Expense>): List<DailyExpense> {
        val dailyExpenses: MutableList<DailyExpense> = mutableListOf()

        val daysInMonth: Int = _getNumberOfDaysInMonth(date.month, date.year)

        for (day in  1..daysInMonth) {
            val createdDate = SimpleDate.initFromDateString("$day/${date.month}/${date.year}")

            val listOfExpensesByDate: List<Expense> = _filterExpensesByDate(expenses, createdDate)

            dailyExpenses.add(DailyExpense(createdDate, listOfExpensesByDate))
        }
        return dailyExpenses
    }

    private fun _filterExpensesByDate(
        expenses: List<Expense>,
        date: SimpleDate
    ): List<Expense> {
        // optimization, day most likely to differ and break the sequence
        return expenses.filter { expense: Expense ->
            expense.day == date.day &&
            expense.month == date.month &&
            expense.year == date.year
        }
    }

    private fun _getNumberOfDaysInMonth(month: Int, year: Int): Int {
        return YearMonth.of(year, month).lengthOfMonth()
    }

    private fun _calculateDailyExpenseStatusesAndAllowedSpending(dailyExpenses: List<DailyExpense>) {
        var oldExceededAmount = 0.0

        for (dailyExpense in dailyExpenses) {

            dailyExpense.allowedSpending = _calculateAllowedSpending(oldExceededAmount)

            val newlyExceededAmount: Double = dailyExpense.total - _dailyExpenseLimit

            val status = _getDailyExpenseStatus(oldExceededAmount, newlyExceededAmount)

            oldExceededAmount += newlyExceededAmount


            if (oldExceededAmount <= 0.0) {
                oldExceededAmount = 0.0
            }

            dailyExpense.status = status
        }
    }

    private fun _getDailyExpenseStatus(
        oldExceededAmount: Double,
        newlyExceededAmount: Double
    ): DailyExpenseStatus {
        var dailyExpenseStatus: DailyExpenseStatus = DailyExpenseStatus.NOT_EXCEEDED

        if (oldExceededAmount > 0.0) {
            dailyExpenseStatus = DailyExpenseStatus.EXCEEDED_PREVIOUSLY
        }

        if (newlyExceededAmount > 0.0) {
            dailyExpenseStatus = DailyExpenseStatus.EXCEEDED
        }
        return dailyExpenseStatus
    }

    private fun _calculateAllowedSpending(oldExceededAmount: Double): Double {
        /*  this default value of 0.0 covers the conditions where daily
        *   expense limit has been exceeded in previous day and situation
        *   where we exceeded daily expense limit that day */
        var allowedSpending = 0.0

        if (abs(oldExceededAmount) < abs(_dailyExpenseLimit)) {
            allowedSpending = _dailyExpenseLimit - oldExceededAmount
        }
        return allowedSpending
    }
}