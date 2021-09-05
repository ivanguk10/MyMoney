package com.example.mymoney.database.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.mymoney.models.MoneyModel
import com.example.mymoney.util.Constants.Companion.MONEY_TABLE
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

@Parcelize
@Entity(tableName = MONEY_TABLE)
data class MoneyEntity(

    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var value: Float = 0f,
    var money: @RawValue MoneyModel
): Parcelable {
}