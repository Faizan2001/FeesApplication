package com.example.feesapplication

import android.app.Application
import com.example.feesapplication.data.database.StudentRoomDatabase

class StudentApplication : Application() {
    val database: StudentRoomDatabase by lazy { StudentRoomDatabase.getDatabase(this) }
}
