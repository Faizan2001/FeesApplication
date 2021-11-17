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
    suspend fun deleteStudent(student: Student)

    @Delete
    suspend fun deleteBatch(batch: Batch)


    @Query("DELETE FROM students WHERE batchName = :batchName ")
    suspend fun deleteAllStudents(batchName: String)


    @Query("SELECT * FROM students ORDER BY studentName ASC")
    fun getAllStudentData(): LiveData<List<Student>>

    @Query("SELECT * FROM batches ORDER BY batchName ASC")
    fun getAllBatchData(): LiveData<List<Batch>>

    @Transaction
    @Query("SELECT * FROM students WHERE batchName = :batchName")
    fun getBatchWithStudents(batchName: String): LiveData<List<Student>>

    @Query("SELECT * FROM batches WHERE batchName LIKE :searchQuery")
    fun searchBatchDatabase(searchQuery: String): LiveData<List<Batch>>

    @Query("SELECT * FROM students WHERE batchName = :vBatchName AND studentName LIKE :searchQuery")
    fun searchStudentInBatch(vBatchName: String, searchQuery: String): LiveData<List<Student>>

    @Query("SELECT * FROM students WHERE studentName LIKE :searchQuery")
    fun searchStudentDatabase(searchQuery: String): LiveData<List<Student>>

    // Sort Fee Status

    @Query("SELECT * FROM students ORDER BY CASE WHEN status LIKE 'U%' THEN 1 WHEN status LIKE 'P%' THEN 2 WHEN status LIKE 'N%' THEN 3 END")
    fun sortByUnpaid(): LiveData<List<Student>>

    @Query("SELECT * FROM students ORDER BY CASE WHEN status LIKE 'P%' THEN 1 WHEN status LIKE 'U%' THEN 2 WHEN status LIKE 'N%' THEN 3 END")
    fun sortByPaid(): LiveData<List<Student>>

    // Sort Days

    @Query("SELECT * FROM batches WHERE batchDays LIKE '%Sunday%'")
    fun sortBySunday(): LiveData<List<Batch>>

    @Query("SELECT * FROM batches WHERE batchDays LIKE '%Monday%'")
    fun sortByMonday(): LiveData<List<Batch>>

    @Query("SELECT * FROM batches WHERE batchDays LIKE '%Tuesday%'")
    fun sortByTuesday(): LiveData<List<Batch>>

    @Query("SELECT * FROM batches WHERE batchDays LIKE '%Wednesday%'")
    fun sortByWednesday(): LiveData<List<Batch>>

    @Query("SELECT * FROM batches WHERE batchDays LIKE '%Thursday%'")
    fun sortByThursday(): LiveData<List<Batch>>

    @Query("SELECT * FROM batches WHERE batchDays LIKE '%Friday%'")
    fun sortByFriday(): LiveData<List<Batch>>

    @Query("SELECT * FROM batches WHERE batchDays LIKE '%Saturday%'")
    fun sortBySaturday(): LiveData<List<Batch>>










}