package com.example.mymoney.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mymoney.models.MoneyModel
import com.example.mymoney.util.Constants
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
@Entity(tableName = Constants.MONEY_TABLE)
data class ExpenseEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var amount: Float = 0f,
    var type: String = "",
    var date: String = ""
): Parcelable {
}
