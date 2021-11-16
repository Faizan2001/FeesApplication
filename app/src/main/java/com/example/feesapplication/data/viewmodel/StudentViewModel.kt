package com.example.feesapplication.data.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.data.database.StudentRoomDatabase
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentViewModel(application: Application): AndroidViewModel(application) {

    private val studentDao = StudentRoomDatabase.getDatabase(application).studentDao()
    private val repository: StudentRepository = StudentRepository(studentDao)

    val getAllStudentData: LiveData<List<Student>> = repository.getAllStudentData
    val getAllBatchData: LiveData<List<Batch>> = repository.getAllBatchData

    val sortByUnpaid: LiveData<List<Student>> = repository.sortByUnpaid
    val sortByPaid: LiveData<List<Student>> = repository.sortByPaid


    fun insertBatch(batch: Batch) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.insertBatch(batch)
        }

    }

    fun insertStudent(student: Student) {

        viewModelScope.launch(Dispatchers.IO) {
            repository.insertStudent(student)
        }

    }

    fun studentsInBatch(batchName: String) : LiveData<List<Student>> {
        return repository.studentsInBatch(batchName)
    }

    fun deleteAllStudents(batchName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllStudents(batchName)
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteStudent(student)
        }
    }

    fun deleteBatch(batch: Batch) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteBatch(batch)
        }
    }

    fun searchBatchDatabase(searchQuery: String): LiveData<List<Batch>>{
        return repository.searchBatchDatabase(searchQuery)
    }

    fun searchStudentInBatch(vBatchName: String, searchQuery: String): LiveData<List<Student>>{
        return repository.searchStudentInBatch(vBatchName, searchQuery)
    }

    fun searchStudentDatabase(searchQuery: String): LiveData<List<Student>> {
        return repository.searchStudentDatabase(searchQuery)
    }




}
