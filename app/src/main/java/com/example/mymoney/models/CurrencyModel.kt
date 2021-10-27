package com.example.mymoney.models


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CurrencyModel(
    @Json(name = "result")
    val result: String,
    @Json(name = "base_code")
    val baseCurrency: String,
    @Json(name = "conversion_rates")
    val conversionRates: ConversionRates,
) {
    @JsonClass(generateAdapter = true)
    data class ConversionRates(

//        Netherlands
        @Json(name = "ANG")
        val aNG: Double,
//        Argentina
        @Json(name = "ARS")
        val aRS: Double,
//        Australian dollar
        @Json(name = "AUD")
        val aUD: Double,
//        Brazil
        @Json(name = "BRL")
        val bRL: Double,
        @Json(name = "BYN")
        val bYN: Double,
//        Canadian dollar
        @Json(name = "CAD")
        val cAD: Double,
//        Swiss Frank
        @Json(name = "CHF")
        val cHF: Double,
//        China
        @Json(name = "CNY")
        val cNY: Double,
//        Czech
        @Json(name = "CZK")
        val cZK: Double,
//        Danish crone
        @Json(name = "DKK")
        val dKK: Double,
        @Json(name = "EUR")
        val eUR: Double,
        @Json(name = "GBP")
        val gBP: Double,
//        Japan
        @Json(name = "JPY")
        val jPY: Double,
//        Kazakhstan
        @Json(name = "KZT")
        val kZT: Double,
//        Norwegian
        @Json(name = "NOK")
        val nOK: Double,
//        New Zealand
        @Json(name = "NZD")
        val nZD: Double,
//        Poland
        @Json(name = "PLN")
        val pLN: Double,
//        Serbian
        @Json(name = "RSD")
        val rSD: Double,
        @Json(name = "RUB")
        val rUB: Double,
//        Swedish
        @Json(name = "SEK")
        val sEK: Double,
//        Singapore
        @Json(name = "SGD")
        val sGD: Double,
        @Json(name = "UAH")
        val uAH: Double,
        @Json(name = "USD")
        val uSD: Int,
    )
}