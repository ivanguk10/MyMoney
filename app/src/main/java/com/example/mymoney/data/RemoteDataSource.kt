package com.example.mymoney.data

import com.example.mymoney.data.network.CurrencyApi
import com.example.mymoney.models.CurrencyModel
import retrofit2.Response
import javax.inject.Inject

class RemoteDataSource @Inject constructor(
    private val currencyApi: CurrencyApi
) {
    suspend fun getCurrencies(baseCurrency: String): Response<CurrencyModel> {
        return currencyApi.getCurrencies(baseCurrency)
    }

}