package com.example.feesapplication.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.feesapplication.data.FeeStatus


@Entity(tableName = "students")
data class Student(
    @PrimaryKey(autoGenerate = false) val studentName: String,
    @ColumnInfo(name = "number") val studentNumber: Int,
    @ColumnInfo(name = "email") val studentEmail: String,
    @ColumnInfo(name = "status") val feesStatus: FeeStatus,
    @ColumnInfo(name = "fees") val feesAmount: Int,
    @ColumnInfo(name = "batchName") val batchName: String
    )

