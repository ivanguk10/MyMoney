package com.example.mymoney

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mymoney.models.IncomeModel
import com.example.mymoney.util.Constants.Companion.MONEY_TABLE
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@Entity(tableName = MONEY_TABLE)
data class MoneyEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var value: Float = 0f,
    //var incomes: @RawValue List<IncomeModel>
): Parcelable {
}