package com.mvpbrosproduction.simpleexpensetracker.api

import com.mvpbrosproduction.simpleexpensetracker.data_models.AlphaVantageResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApiService {
    @GET("query")
    suspend fun getStockQuote(
        @Query("function") function: String = "GLOBAL_QUOTE",
        @Query("symbol") symbol: String,
        @Query("apikey") apiKey: String
    ): AlphaVantageResponse
}
