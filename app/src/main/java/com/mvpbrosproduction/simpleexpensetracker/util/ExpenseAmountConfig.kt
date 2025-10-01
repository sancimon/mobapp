package com.mvpbrosproduction.simpleexpensetracker.util

import android.util.Log
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.mvpbrosproduction.simpleexpensetracker.constants.DataStoreKeys
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlin.math.pow

private const val TAG = "ExpenseAmountConfig"

class ExpenseAmountConfig private constructor(dataStore: DataStore<Preferences>) {

    private val _dataStore: DataStore<Preferences> = dataStore

    companion object {
        @Volatile
        private var instance: ExpenseAmountConfig? = null;

        fun getInstance(dataStore: DataStore<Preferences>) =
            instance ?: synchronized(this) {
                instance ?: ExpenseAmountConfig(dataStore).also { instance = it }
            }
    }

    private val MAX_NUMBER_OF_DIGITS: Int = 4
    private val NO_MULTIPLIER: Int = 1
    private val THOUSAND: Int = 1000
    private val SINGLE_DIGIT: Int = 1

    private var _numOfWholeNumberDigits: Int = runBlocking {
        _dataStore.data.first() }[DataStoreKeys.NUM_OF_WHOLE_NUMBER_DIGITS] ?: 4

    private var _multiplier: Int = runBlocking {
        _dataStore.data.first() }[DataStoreKeys.MULTIPLIER] ?: 1

    public val digits = mutableStateListOf(0,0,0,0)

    public val digitsState = mutableIntStateOf(_numOfWholeNumberDigits)
    public val multiplierState = mutableIntStateOf(_multiplier)
    public val total = mutableDoubleStateOf(0.0)

    public val maxNumberOfDigits: Int
        get() = this.MAX_NUMBER_OF_DIGITS

    public fun increaseStep() {
        if (_multiplier == NO_MULTIPLIER) {
            if (_numOfWholeNumberDigits != MAX_NUMBER_OF_DIGITS) {
                _numOfWholeNumberDigits++
            } else {
                _numOfWholeNumberDigits = 2
                _multiplier *= THOUSAND
            }
        } else {
            if (_numOfWholeNumberDigits != (MAX_NUMBER_OF_DIGITS - 1)) {
                _numOfWholeNumberDigits++
            } else {
                _numOfWholeNumberDigits = 1
                _multiplier *= THOUSAND
            }
        }

        digitsState.intValue = _numOfWholeNumberDigits
        multiplierState.intValue = _multiplier

        _updateStore()

        Log.d(TAG, "Whole digits: $_numOfWholeNumberDigits; Multiplier: $_multiplier")
    }

    public fun decreaseStep() {
        if (_multiplier == NO_MULTIPLIER && _numOfWholeNumberDigits == SINGLE_DIGIT) {
            return
        }
        if (_multiplier == THOUSAND) {
            if (_numOfWholeNumberDigits != (SINGLE_DIGIT + 1)) {
                _numOfWholeNumberDigits--
            } else {
                _numOfWholeNumberDigits = 4
                _multiplier /= THOUSAND
            }
        } else {
            if (_numOfWholeNumberDigits != SINGLE_DIGIT) {
                _numOfWholeNumberDigits--
            } else {
                _numOfWholeNumberDigits = 3
                _multiplier /= THOUSAND
            }
        }

        digitsState.intValue = _numOfWholeNumberDigits
        multiplierState.intValue = _multiplier

        _updateStore()

        Log.d(TAG, "Whole digits: $_numOfWholeNumberDigits; Multiplier: $_multiplier")
    }

    public fun calculateTotal() {
        val base = getDigitArrayBase()

        val exponent = MAX_NUMBER_OF_DIGITS - _numOfWholeNumberDigits

        total.doubleValue = base / 10.0.pow(exponent.toDouble()) * _multiplier
    }

    public fun clearAmount() {
        digits.fill(0)
        total.doubleValue = 0.0
    }

    private fun getDigitArrayBase(): Double {
        var base = 0.0
        digits.forEachIndexed { index, digit ->
            val exponent = digits.size - index - 1
            base += digit * 10.0.pow(exponent.toDouble())
        }
        return base
    }

    private fun _updateStore() {
        runBlocking {
            _dataStore.edit { variables ->
                variables[DataStoreKeys.NUM_OF_WHOLE_NUMBER_DIGITS] = _numOfWholeNumberDigits
                variables[DataStoreKeys.MULTIPLIER] = _multiplier
            }
        }
    }

}