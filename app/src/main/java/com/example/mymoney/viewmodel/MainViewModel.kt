package com.example.mymoney.viewmodel

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.mymoney.data.database.entities.MoneyEntity
import com.example.mymoney.models.CurrencyModel
import com.example.mymoney.repository.Repository
import com.example.mymoney.util.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository,
    application: Application
): AndroidViewModel(application) {

//    Room

    val readMoney: LiveData<List<MoneyEntity>> = repository.local.readMoney()

    fun insertMoneyEntity(moneyEntity: MoneyEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.insertMoneyEntity(moneyEntity)
        }
    }

    fun updateMoneyEntity(moneyEntity: MoneyEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.local.updateMoneyEntity(moneyEntity)
        }
    }

//    Retrofit

    private var _currenciesResponse: MutableLiveData<NetworkResult<CurrencyModel>> = MutableLiveData()
    val currenciesResponse: LiveData<NetworkResult<CurrencyModel>> get() = _currenciesResponse

    fun getCurrencies(baseCurrency: String) = viewModelScope.launch(Dispatchers.IO) {
        getCurrenciesSafeCall(baseCurrency)
    }

    private suspend fun getCurrenciesSafeCall(baseCurrency: String) {
        _currenciesResponse.postValue(NetworkResult.Loading())

        if (hasInternetConnection()) {
            try {
                val response = repository.remote.getCurrencies(baseCurrency)
                _currenciesResponse.postValue(handleCurrenciesResponse(response))
                val currencies = currenciesResponse.value!!.data

            }
            catch (e: Exception) {
                _currenciesResponse.postValue(NetworkResult.Error("Currencies not found."))
            }
        } else {
            _currenciesResponse.value = NetworkResult.Error("No Internet Connection.")
        }
    }

    private fun handleCurrenciesResponse(response: Response<CurrencyModel>): NetworkResult<CurrencyModel> {
        when {
            response.message().toString().contains("timeout") -> {
                return NetworkResult.Error("Timeout")
            }
            response.code() == 402 -> {
                return NetworkResult.Error("API Key Limited.")
            }
            response.body()!!.result.isEmpty() -> {
                return NetworkResult.Error("Currencies not found.")
            }
            response.isSuccessful -> {
                val foodRecipes = response.body()
                return NetworkResult.Success(foodRecipes!!)
            }
            else -> {
                return NetworkResult.Error(response.message())
            }
        }
    }

    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<Application>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val activeNetwork = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }

}