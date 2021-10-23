package com.example.feesapplication.data.database


import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface StudentDao{

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(student: Student)

    @Update
    suspend fun update(student: Student)

    @Delete
    suspend fun delete(student: Student)

    // Flow used instead of LiveData

    @Query("SELECT * FROM students ORDER BY id ASC")
    fun getAllData(): LiveData<List<Student>>







}