package com.mvpbrosproduction.simpleexpensetracker.objects

import com.mvpbrosproduction.simpleexpensetracker.R
import com.mvpbrosproduction.simpleexpensetracker.data_models.DefaultExpenseCategory

object DefaultExpenseCategories {
    private const val APPAREL = "apparel"
    private const val BEER = "beer"
    private const val CIGARETTES = "cigarettes"
    private const val COFFEE = "coffee"
    private const val DINING = "dining"
    private const val FRUIT = "fruit"
    private const val GAS = "gas"
    private const val GROCERIES = "groceries"
    private const val TOOLS = "tools"
    private const val MEDICATION = "medication"
    private const val TRANSPORTATION = "transportation"
    private const val TOYS = "toys"
    private const val CHILD_CARE = "child care"
    private const val GIFT = "gift"
    private const val ENTERTAINMENT = "entertainment"
    private const val NO_CATEGORY_FOUND = "no_category_found"

    val map = mapOf(
        APPAREL to DefaultExpenseCategory(APPAREL, R.drawable.apparel, "some description", false),
        BEER to DefaultExpenseCategory(BEER, R.drawable.beer, "some description", true),
        CIGARETTES to DefaultExpenseCategory(CIGARETTES, R.drawable.cigarettes, "some description", false),
        COFFEE to DefaultExpenseCategory(COFFEE, R.drawable.coffee, "some description", false),
        DINING to DefaultExpenseCategory(DINING, R.drawable.dining, "some description", true),
        ENTERTAINMENT to DefaultExpenseCategory(ENTERTAINMENT, R.drawable.entertainment, "some description", true),
        FRUIT to DefaultExpenseCategory(FRUIT, R.drawable.fruit, "some description", false),
        GAS to DefaultExpenseCategory(GAS, R.drawable.gas, "some description", false),
        GROCERIES to DefaultExpenseCategory(GROCERIES, R.drawable.groceries, "some description", true),
        TOOLS to DefaultExpenseCategory(TOOLS, R.drawable.tools, "some description", false),
        TRANSPORTATION to DefaultExpenseCategory(TRANSPORTATION, R.drawable.transportation, "some description", false),
        MEDICATION to DefaultExpenseCategory(MEDICATION, R.drawable.medication, "some description", false),
        TOYS to DefaultExpenseCategory(TOYS, R.drawable.toys, "some description", false),
        CHILD_CARE to DefaultExpenseCategory(CHILD_CARE, R.drawable.child_care, "some description", false),
        GIFT to DefaultExpenseCategory(GIFT, R.drawable.gift, "some description", false)
    )

    val noCategoryFound = DefaultExpenseCategory(NO_CATEGORY_FOUND, R.drawable.no_category_found, "some description", false)
}