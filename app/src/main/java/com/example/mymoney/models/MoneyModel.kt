package com.example.mymoney.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
data class MoneyModel(
    //@SerializedName("incomes")
    var incomes: @RawValue ArrayList<IncomeModel>,

    //@SerializedName("expenses")
    var expenses: @RawValue ArrayList<ExpenseModel>
): Parcelable {

}