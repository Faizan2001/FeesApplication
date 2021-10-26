package com.example.feesapplication.data.database.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.data.database.entities.Student

data class BatchWithStudents(
    @Embedded val batch : Batch,
    @Relation(
        parentColumn = "batchName",
        entityColumn = "batchName"
    )

    val students : List<Student>
)
