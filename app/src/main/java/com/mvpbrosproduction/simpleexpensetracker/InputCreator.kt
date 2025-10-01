package com.mvpbrosproduction.simpleexpensetracker

import android.location.Location
import android.util.Log
import java.util.Locale

private const val TAG = "InputCreator"

class InputCreator(private val location: Location?) {
    private val displayLanguage: String by lazy { Locale.getDefault().displayLanguage }


    fun log() {

        Log.d(TAG, "display language: $displayLanguage, location: $location")
    }
}