package com.example.feesapplication.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.feesapplication.data.database.entities.Student

class StudentDiffUtil(
    private val oldList: List<Student>,
    private val newList: List<Student>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] === newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].studentName == newList[newItemPosition].studentName
                && oldList[oldItemPosition].studentNumber == newList[newItemPosition].studentNumber
                && oldList[oldItemPosition].feesAmount == newList[newItemPosition].feesAmount
                && oldList[oldItemPosition].feesStatus == newList[newItemPosition].feesStatus
                && oldList[oldItemPosition].studentEmail == newList[newItemPosition].studentEmail
                && oldList[oldItemPosition].batchName == newList[newItemPosition].batchName


    }
}