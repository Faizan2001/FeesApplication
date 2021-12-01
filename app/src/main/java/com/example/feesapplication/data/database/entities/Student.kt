package com.example.feesapplication.data.database.entities

import android.os.Parcelable
import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.feesapplication.data.database.FeeStatus
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = false) val studentName: String,
    @ColumnInfo(name = "number") val studentNumber: Long,
    @ColumnInfo(name = "fees") val feesAmount: Long,
    @ColumnInfo(name = "status") var feesStatus: FeeStatus,
    @ColumnInfo(name = "email") val studentEmail: String,
    @ColumnInfo(name = "batchName") val batchName: String,
    @ColumnInfo(name = "monthsPaid") val monthsPaid: String
) : Parcelable


