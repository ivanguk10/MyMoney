package com.example.mymoney.database

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.example.mymoney.util.Constants.Companion.DEFAULT_TYPE
import com.example.mymoney.util.Constants.Companion.PREFERENCES_NAME
import com.example.mymoney.util.Constants.Companion.PREFERENCES_TYPE
import com.example.mymoney.util.Constants.Companion.PREFERENCES_TYPE_ID
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
        val selectedType = stringPreferencesKey(PREFERENCES_TYPE)
        val selectedTypeId = intPreferencesKey(PREFERENCES_TYPE_ID)
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveExpenseType(type: String, typeId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.selectedType] = type
            preferences[PreferencesKeys.selectedTypeId] = typeId
        }
    }

    val readExpenseType: Flow<ExpenseType> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedType = preferences[PreferencesKeys.selectedType] ?: DEFAULT_TYPE
            val selectedTypeId = preferences[PreferencesKeys.selectedTypeId] ?: 0
            ExpenseType(
                selectedType,
                selectedTypeId
            )
        }


}

data class ExpenseType(
    val selectedType: String,
    val selectedTypeId: Int
)