package com.example.mymoney.models

import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.RawValue

data class MoneyModel(
    //@SerializedName("incomes")
    var incomes: @RawValue List<IncomeModel>,

    //@SerializedName("expenses")
    var expenses: @RawValue List<ExpenseModel>
) {

}