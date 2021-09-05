package com.example.mymoney.repository

import com.example.mymoney.database.LocalDataSource
import com.example.mymoney.di.DatabaseModule
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(
    localDataSource: LocalDataSource
) {
    val local = localDataSource
}