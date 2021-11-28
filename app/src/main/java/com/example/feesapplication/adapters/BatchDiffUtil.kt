package com.example.feesapplication.adapters

import androidx.recyclerview.widget.DiffUtil
import com.example.feesapplication.data.database.entities.Batch

class BatchDiffUtil(
    private val oldList: List<Batch>,
    private val newList: List<Batch>
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
        return oldList[oldItemPosition].batchName == newList[newItemPosition].batchName
                && oldList[oldItemPosition].batchTime == newList[newItemPosition].batchTime
                && oldList[oldItemPosition].batchDays == newList[newItemPosition].batchDays
    }
}