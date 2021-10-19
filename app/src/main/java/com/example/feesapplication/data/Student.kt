package com.example.feesapplication.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = true) val id:Int = 0,
    @ColumnInfo(name = "name") val studentName: String,
    @ColumnInfo(name = "number") val studentNumber: Int,
    @ColumnInfo(name = "email") val studentEmail: String,
    @ColumnInfo(name = "status") val feesStatus: Boolean,
    @ColumnInfo(name = "fees") val feesAmount: Int,
    @ColumnInfo(name = "batch") val batchName: String)

