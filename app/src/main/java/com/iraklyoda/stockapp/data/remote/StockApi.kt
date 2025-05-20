package com.iraklyoda.stockapp.data.remote

import com.iraklyoda.stockapp.BuildConfig
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface StockApi {

    @GET("query?function=LISTING_STATUS")
    suspend fun getListings(
        @Query("apiKey") apiKey: String = BuildConfig.ALPHA_VANTAGE_API_KEY
    ) : ResponseBody
}