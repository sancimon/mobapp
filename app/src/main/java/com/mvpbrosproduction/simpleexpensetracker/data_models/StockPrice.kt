package com.mvpbrosproduction.simpleexpensetracker.data_models

import com.google.gson.annotations.SerializedName

data class StockPrice(
    val symbol: String,
    val price: Double,
    val change: Double,
    val changePercent: Double,
    val lastUpdated: String
)

// Alpha Vantage API Response Models
data class AlphaVantageResponse(
    @SerializedName("Global Quote")
    val globalQuote: GlobalQuote?
)

data class GlobalQuote(
    @SerializedName("01. symbol")
    val symbol: String?,

    @SerializedName("05. price")
    val price: String?,

    @SerializedName("09. change")
    val change: String?,

    @SerializedName("10. change percent")
    val changePercent: String?,

    @SerializedName("07. latest trading day")
    val latestTradingDay: String?
)
