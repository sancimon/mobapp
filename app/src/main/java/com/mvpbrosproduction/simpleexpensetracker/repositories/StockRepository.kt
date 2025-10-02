package com.mvpbrosproduction.simpleexpensetracker.repositories

import android.util.Log
import com.mvpbrosproduction.simpleexpensetracker.api.StockApiService
import com.mvpbrosproduction.simpleexpensetracker.data_models.StockPrice
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

class StockRepository {
    companion object {
        private const val BASE_URL = "https://api.coingecko.com/api/v3/"
        private const val TAG = "StockRepository"
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
            // Using CoinGecko free API - Bitcoin price (no auth required)
            val response = apiService.getCryptoPrice(
                ids = "bitcoin",
                vsCurrencies = "usd",
                include24hrChange = true,
                includeLastUpdatedAt = true
            )

            val bitcoin = response.bitcoin
            if (bitcoin != null && bitcoin.usd != null) {
                val price = bitcoin.usd ?: 0.0
                val changePercent = bitcoin.usd24hChange ?: 0.0
                val change = price * (changePercent / 100) // Calculate absolute change

                val lastUpdated = bitcoin.lastUpdatedAt?.let { timestamp ->
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US)
                    sdf.format(Date(timestamp * 1000))
                } ?: "N/A"

                val stockPrice = StockPrice(
                    symbol = "Bitcoin (BTC)",
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
            Log.e(TAG, "Error fetching crypto price", e)
            Result.failure(e)
        }
    }
}
