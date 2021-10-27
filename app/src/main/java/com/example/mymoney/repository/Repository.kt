package com.example.mymoney.repository

import com.example.mymoney.data.LocalDataSource
import com.example.mymoney.data.RemoteDataSource
import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject

@ViewModelScoped
class Repository @Inject constructor(
    localDataSource: LocalDataSource,
    remoteDataSource: RemoteDataSource
) {
    val local = localDataSource
    val remote = remoteDataSource
}