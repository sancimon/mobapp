package com.mvpbrosproduction.simpleexpensetracker.managers

import android.util.Log
import androidx.compose.runtime.MutableDoubleState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.mvpbrosproduction.simpleexpensetracker.models.DailyExpense
import com.mvpbrosproduction.simpleexpensetracker.data_models.Expense
import com.mvpbrosproduction.simpleexpensetracker.models.MonthlyExpense
import com.mvpbrosproduction.simpleexpensetracker.models.SimpleDate
import com.mvpbrosproduction.simpleexpensetracker.repositories.ExpenseRepository

private const val TAG = "ExpenseManager"

class ExpenseManager constructor(
    private val _expenseRepository: ExpenseRepository,
    public val dailyExpenseLimit: MutableDoubleState,
) {
    private val _dateManager: DateManager = DateManager.getInstance()

    public val dailyExpense: MutableState<DailyExpense> =
        mutableStateOf(DailyExpense(_dateManager.selectedDate, listOf()))

    public val monthlyExpense: MutableState<MonthlyExpense> =
        mutableStateOf(
            MonthlyExpense(
                _dateManager.selectedDate,
                listOf(),
                dailyExpenseLimit.doubleValue
            )
        )

    init {
        // init daily expenses
        _reloadDailyExpenses()

        // handle subsequent changes in selected date
        _dateManager.addDateChangeListener { newDate ->
            val updatedListOfExpensesForDate: List<Expense> = _loadDailyExpenses(newDate)

            dailyExpense.value = DailyExpense(newDate, updatedListOfExpensesForDate)
        }

        // init monthly expenses
        _reloadMonthlyExpenses()

        // handle subsequent changes in selected month
        _dateManager.addMonthChangeListener { newDate ->

            val listOfExpensesForMonth: List<Expense> = _loadMonthlyExpenses(newDate)

            monthlyExpense.value = MonthlyExpense(
                newDate, listOfExpensesForMonth, dailyExpenseLimit.doubleValue
            )
        }
    }

    public fun reloadDailyAndMonthlyExpenses() {
        _reloadDailyExpenses()
        _reloadMonthlyExpenses()
    }

    public suspend fun addExpense(expense: Expense): Boolean {
        val expenseId: Long = _expenseRepository.addExpense(expense.toDto())

        Log.d(TAG, "addExpense: $expenseId")

        if (expenseId != -1L) {
            _reloadDailyExpenses()
            _reloadMonthlyExpenses()
        }

        return expenseId != -1L
    }

    public fun deleteExpense(expense: Expense): Boolean {
        val numOfRowsDeleted = _expenseRepository.deleteExpense(expense)

        if (numOfRowsDeleted > 0) {
            _reloadDailyExpenses()
            _reloadMonthlyExpenses()
        }

        return numOfRowsDeleted > 0
    }

    public fun editExpense(expense: Expense): Boolean {
        val numOfEditedRows: Int = _expenseRepository.updateExpense(expense)

        if (numOfEditedRows > 0) {
            _reloadDailyExpenses()
            _reloadMonthlyExpenses()
        }

        return numOfEditedRows > 0
    }

    private fun _loadDailyExpenses(date: SimpleDate): List<Expense> {
        return _expenseRepository.loadDailyExpenses(
            date.day, date.month, date.year
        )
    }

    private fun _loadMonthlyExpenses(date: SimpleDate): List<Expense> {
        return _expenseRepository.loadMonthlyExpenses(
            date.month, date.year
        )
    }

    private fun _reloadDailyExpenses() {
        val listOfExpensesForDate: List<Expense> = _loadDailyExpenses(_dateManager.selectedDate)

        Log.d(TAG, "_reloadDailyExpenses: $listOfExpensesForDate")

        dailyExpense.value = DailyExpense(_dateManager.selectedDate, listOfExpensesForDate)
    }

    private fun _reloadMonthlyExpenses() {
        val listOfExpensesForMonth: List<Expense> = _loadMonthlyExpenses(_dateManager.selectedDate)

        monthlyExpense.value = MonthlyExpense(
            _dateManager.selectedDate, listOfExpensesForMonth, dailyExpenseLimit.doubleValue
        )
    }
}