package com.mvpbrosproduction.simpleexpensetracker.constants

import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey

class DataStoreKeys private constructor() {
    companion object {
        val EXPENSE_LIMIT = doublePreferencesKey("expense_limit")
        val NUM_OF_WHOLE_NUMBER_DIGITS = intPreferencesKey("num_of_whole_number_digits")
        val MULTIPLIER = intPreferencesKey("multiplier")
    }
}