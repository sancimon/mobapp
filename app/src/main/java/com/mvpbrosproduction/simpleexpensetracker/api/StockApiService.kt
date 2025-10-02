package com.mvpbrosproduction.simpleexpensetracker.api

import com.mvpbrosproduction.simpleexpensetracker.data_models.CoinGeckoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApiService {
    @GET("simple/price")
    suspend fun getCryptoPrice(
        @Query("ids") ids: String,
        @Query("vs_currencies") vsCurrencies: String,
        @Query("include_24hr_change") include24hrChange: Boolean = true,
        @Query("include_last_updated_at") includeLastUpdatedAt: Boolean = true
    ): CoinGeckoResponse
}
