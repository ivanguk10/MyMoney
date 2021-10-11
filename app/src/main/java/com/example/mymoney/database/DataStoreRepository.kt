package com.example.mymoney.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.mymoney.util.Constants.Companion.PREFERENCES_CARD_ID
import com.example.mymoney.util.Constants.Companion.PREFERENCES_MONTH_ID
import com.example.mymoney.util.Constants.Companion.PREFERENCES_NAME
import com.example.mymoney.util.Constants.Companion.PREFERENCES_SORT_ID
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context){

    private object PreferencesKeys {
        val cardId = intPreferencesKey(PREFERENCES_CARD_ID)
        val sortId = intPreferencesKey(PREFERENCES_SORT_ID)

        val monthId = intPreferencesKey(PREFERENCES_MONTH_ID)
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveSortType(cardId: Int, sortId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.cardId] = cardId
            preferences[PreferencesKeys.sortId] = sortId
        }
    }

    suspend fun saveMonth(monthId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.monthId] = monthId
        }
    }

    val readSortType: Flow<SortCardType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val cardId = preferences[PreferencesKeys.cardId] ?: 0
            val sortId = preferences[PreferencesKeys.sortId] ?: 0
            SortCardType(
                cardId,
                sortId
            )
        }

    val readMonth: Flow<MonthValue> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val monthId = preferences[PreferencesKeys.monthId] ?: 10
            MonthValue(
                monthId
            )
        }
}

data class SortCardType(
    val cardId: Int,
    val sortId: Int
)

data class MonthValue(
    val monthId: Int
)