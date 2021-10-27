package com.example.mymoney.data.network

import com.example.mymoney.models.CurrencyModel
import com.example.mymoney.util.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyApi {

    @GET("$API_KEY/latest/{baseCurrency}")
    suspend fun getCurrencies(
        @Path("baseCurrency") baseCurrency: String
    ): Response<CurrencyModel>


}