package com.mvpbrosproduction.simpleexpensetracker.data_models

import com.google.gson.annotations.SerializedName

data class StockPrice(
    val symbol: String,
    val price: Double,
    val change: Double,
    val changePercent: Double,
    val lastUpdated: String
)

// CoinGecko API Response Models (Free, No Auth)
data class CoinGeckoResponse(
    val bitcoin: CryptoData?
)

data class CryptoData(
    val usd: Double?,
    @SerializedName("usd_24h_change")
    val usd24hChange: Double?,
    @SerializedName("last_updated_at")
    val lastUpdatedAt: Long?
)
