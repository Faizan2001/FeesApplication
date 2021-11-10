package com.example.feesapplication.data.database



import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.database.entities.Student
import kotlinx.coroutines.flow.Flow


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

    @Query("DELETE FROM students WHERE batchName = :batchName ")
    suspend fun deleteAllStudents(batchName: String)

    @Query("SELECT * FROM students ORDER BY studentName ASC")
    fun getAllStudentData(): LiveData<List<Student>>

    @Query("SELECT * FROM batches ORDER BY batchName ASC")
    fun getAllBatchData(): LiveData<List<Batch>>

    @Transaction
    @Query("SELECT * FROM students WHERE batchName = :batchName")
    fun getBatchWithStudents(batchName: String): LiveData<List<Student>>







}