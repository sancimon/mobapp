package com.mvpbrosproduction.simpleexpensetracker.util

import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.graphics.Color
import java.lang.StringBuilder
import kotlin.math.pow

class Util {
    companion object {
        private const val MAX_MODULUS: Int = 1000;

        fun convertNumberToDigitsList(number: Int): SnapshotStateList<Int> {
            val digitsList: MutableList<Int> = mutableListOf()
            for (i in 0..3) {
                val divider = MAX_MODULUS / (10.0.pow(i.toDouble()))

                val digit = (number / divider).toInt() % 10

                digitsList.add(digit);
            }
            return digitsList.toMutableStateList()
        }

        fun getExpenseCategoryIconTextColor(tint: Float): Color {
            return if (tint > 200f && tint < 290f) {
                Color.White
            } else {
                Color.Black
            }
        }

        fun extractInitials(text: String): String {
            val words = text.split(" ")
            val initials = StringBuilder()

            for (word in words) {
                if (word.isNotEmpty()) {
                    val firstChar: Char = word[0]
                    initials.append(firstChar.uppercaseChar())

                    if (initials.length == 2) {
                        break
                    }
                }
            }
            return initials.toString()
        }
    }
}