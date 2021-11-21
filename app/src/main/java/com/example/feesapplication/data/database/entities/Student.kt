package com.example.feesapplication.data.database.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.feesapplication.data.database.FeeStatus
import kotlinx.parcelize.Parcelize


@Entity(tableName = "students")
@Parcelize
data class Student(
    @PrimaryKey(autoGenerate = false) val studentName: String,
    @ColumnInfo(name = "number") val studentNumber: Long,
    @ColumnInfo(name = "fees") val feesAmount: Double,
    @ColumnInfo(name = "status") var feesStatus: FeeStatus,
    @ColumnInfo(name = "email") val studentEmail: String,
    @ColumnInfo(name = "batchName") val batchName: String) : Parcelable

