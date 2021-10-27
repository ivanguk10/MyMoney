package com.example.mymoney.data.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mymoney.models.MoneyModel
import com.example.mymoney.util.Constants
import com.example.mymoney.util.Constants.Companion.EXPENSE_TABLE
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


@Parcelize
@Entity(tableName = EXPENSE_TABLE)
data class ExpenseEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var amount: Float = 0f,
    var type: String = "",
    var date: String = ""
): Parcelable {
}
