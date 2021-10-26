package com.example.feesapplication.data.database


import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.data.database.relation.BatchWithStudents


@Dao
interface StudentDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBatch(batch: Batch)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStudent(student: Student)

    @Update
    suspend fun update(student: Student)

    @Delete
    suspend fun delete(student: Student)

    @Query("SELECT * FROM students ORDER BY studentName ASC")
    fun getAllData(): LiveData<List<Student>>

    @Transaction
    @Query("SELECT * FROM batches WHERE batchName = :batchName")
    suspend fun getBatchWithStudents(batchName: String): List<BatchWithStudents>







}