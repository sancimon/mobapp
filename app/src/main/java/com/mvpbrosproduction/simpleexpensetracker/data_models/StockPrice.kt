package com.mvpbrosproduction.simpleexpensetracker.data_models

data class StockPrice(
    val symbol: String,
    val price: Double,
    val change: Double,
    val changePercent: Double,
    val lastUpdated: String
)

// Alpha Vantage API Response Models
data class AlphaVantageResponse(
    val `Global Quote`: GlobalQuote?
)

data class GlobalQuote(
    val `01. symbol`: String?,
    val `05. price`: String?,
    val `09. change`: String?,
    val `10. change percent`: String?,
    val `07. latest trading day`: String?
)
