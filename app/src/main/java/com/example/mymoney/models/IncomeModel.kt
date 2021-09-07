package com.example.mymoney.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class IncomeModel(
    var amount: Float = 0f
): Parcelable {
}