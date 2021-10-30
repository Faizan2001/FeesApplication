package com.example.feesapplication.data.database

import android.content.Context
import androidx.room.*
import com.example.feesapplication.data.Converter
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.database.entities.Student

@Database(entities = [Student::class, Batch::class], version = 4, exportSchema = false)
@TypeConverters(Converter::class)
abstract class StudentRoomDatabase : RoomDatabase() {

    abstract fun studentDao() : StudentDao

    companion object {
        @Volatile
        private var INSTANCE: StudentRoomDatabase? = null
        fun getDatabase(context: Context): StudentRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                   StudentRoomDatabase::class.java,
                    "student_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                return instance
            }
        }
    }



}