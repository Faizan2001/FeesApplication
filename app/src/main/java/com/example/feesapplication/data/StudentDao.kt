package com.example.feesapplication.data

import android.content.ClipData
import androidx.room.*


@Dao
interface StudentDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(student: Student)

    @Update
    suspend fun update(student: Student)

    @Delete
    suspend fun delete(student: Student)







}