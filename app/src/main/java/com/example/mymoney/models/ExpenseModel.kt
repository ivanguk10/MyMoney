package com.example.mymoney.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ExpenseModel(
    var amount: Float = 0f,
    var type: String = ""
): Parcelable {

}