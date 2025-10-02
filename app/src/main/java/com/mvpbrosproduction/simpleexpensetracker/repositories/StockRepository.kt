package com.mvpbrosproduction.simpleexpensetracker.repositories

import android.util.Log
import com.mvpbrosproduction.simpleexpensetracker.api.StockApiService
import com.mvpbrosproduction.simpleexpensetracker.data_models.StockPrice
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class StockRepository {
    companion object {
        private const val BASE_URL = "https://www.alphavantage.co/"
        private const val TAG = "StockRepository"

        // Free API key from Alpha Vantage - get your own at https://www.alphavantage.co/support/#api-key
        private const val API_KEY = "demo" // Replace with actual API key
    }

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(StockApiService::class.java)

    suspend fun getSP500Price(): Result<StockPrice> {
        return try {
            // Using SPY (S&P 500 ETF) as proxy for S&P 500 index
            val response = apiService.getStockQuote(symbol = "SPY", apiKey = API_KEY)

            val quote = response.`Global Quote`
            if (quote != null && quote.`05. price` != null) {
                val price = quote.`05. price`?.toDoubleOrNull() ?: 0.0
                val change = quote.`09. change`?.toDoubleOrNull() ?: 0.0
                val changePercent = quote.`10. change percent`?.replace("%", "")?.toDoubleOrNull() ?: 0.0
                val lastUpdated = quote.`07. latest trading day` ?: "N/A"

                val stockPrice = StockPrice(
                    symbol = "SPY (S&P 500)",
                    price = price,
                    change = change,
                    changePercent = changePercent,
                    lastUpdated = lastUpdated
                )
                Result.success(stockPrice)
            } else {
                Log.e(TAG, "Invalid response from API")
                Result.failure(Exception("Invalid API response"))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching stock price", e)
            Result.failure(e)
        }
    }
}
