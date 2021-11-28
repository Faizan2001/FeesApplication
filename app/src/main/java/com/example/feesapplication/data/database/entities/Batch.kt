package com.example.feesapplication.data.database.entities


import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "batches")
data class Batch(
    @PrimaryKey(autoGenerate = false) val batchName: String,
    @ColumnInfo(name = "batchTime") val batchTime: String,
    @ColumnInfo(name = "batchDays") val batchDays: String
) : Parcelable
