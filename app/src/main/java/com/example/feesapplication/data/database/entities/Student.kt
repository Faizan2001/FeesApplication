package com.example.feesapplication.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.feesapplication.data.FeeStatus
import java.math.BigInteger


@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = false) val studentName: String,
    @ColumnInfo(name = "number") val studentNumber: Long,
    @ColumnInfo(name = "fees") val feesAmount: Double,
    @ColumnInfo(name = "status") val feesStatus: FeeStatus,
    @ColumnInfo(name = "email") val studentEmail: String,
    @ColumnInfo(name = "batchName") val batchName: String)

