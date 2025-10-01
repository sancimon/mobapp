package com.mvpbrosproduction.simpleexpensetracker.models

import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class SimpleDate constructor(date: Date) {
    public val displayDate: String = SimpleDateFormat("dd/MM/yyyy").format(date)

    public val msSinceEpoch: Long = date.time

    companion object {
        // e.g. "15/3/2024" as input
        public fun initFromDateString(dateString: String): SimpleDate {
            val formatter = SimpleDateFormat("dd/MM/yyyy")

            val date = formatter.parse(dateString)

            return SimpleDate(date)
        }
    }

    public val day: Int
        get() {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            return LocalDate.parse(displayDate, formatter).dayOfMonth
        }

    public val month: Int
        get() {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            return LocalDate.parse(displayDate, formatter).monthValue
        }

    public val year: Int
        get() {
            val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            return LocalDate.parse(displayDate, formatter).year
        }

    override fun equals(other: Any?): Boolean {
        // referential equality
        if (this === other) return true

        // type equality
        if (other !is SimpleDate) return false

        // check if date strings are the same
        return this.displayDate == other.displayDate
    }

    override fun toString(): String {
        return "SimpleDate(displayDate='$displayDate')"
    }
}