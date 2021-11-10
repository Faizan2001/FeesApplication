package com.example.feesapplication

import android.app.Application
import androidx.lifecycle.*
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.data.database.StudentRoomDatabase
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.repository.StudentRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class StudentViewModel(application: Application): AndroidViewModel(application) {

    private val studentDao = StudentRoomDatabase.getDatabase(application).studentDao()
    private val repository: StudentRepository = StudentRepository(studentDao)

    val getAllStudentData: LiveData<List<Student>> = repository.getAllStudentData
    val getAllBatchData: LiveData<List<Batch>> = repository.getAllBatchData


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




}
