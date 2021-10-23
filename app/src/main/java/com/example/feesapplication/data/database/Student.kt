package com.example.feesapplication.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.feesapplication.data.FeeStatus


@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "name") val studentName: String,
    @ColumnInfo(name = "number") val studentNumber: Int,
    @ColumnInfo(name = "email") val studentEmail: String,
    @ColumnInfo(name = "status") val feesStatus: FeeStatus,
    @ColumnInfo(name = "fees") val feesAmount: Int,
    @ColumnInfo(name = "batch") val batchName: String,
    @ColumnInfo(name = "batch_time") val batchTime: String,
    @ColumnInfo(name = "batch_days") val batchDays: String)

