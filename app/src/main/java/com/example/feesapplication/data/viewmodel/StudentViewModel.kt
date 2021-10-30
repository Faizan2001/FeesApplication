package com.example.feesapplication

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
    private val repository: StudentRepository

    val getAllStudentData: LiveData<List<Student>>
     val getAllBatchData: LiveData<List<Batch>>


    init {
        repository = StudentRepository(studentDao)
        getAllStudentData = repository.getAllStudentData
        getAllBatchData = repository.getAllBatchData
    }

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



}
