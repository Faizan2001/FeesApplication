package com.example.feesapplication.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.feesapplication.data.database.entities.Batch
import com.example.feesapplication.databinding.RowLayoutBinding


class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var batchList = emptyList<Batch>()

    class MyViewHolder(private val binding: RowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(batch: Batch) {
            binding.batchInfo = batch
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(
                    binding
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun getItemCount(): Int {
        return batchList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentBatch = batchList[position]
        holder.bind(currentBatch)


    }

    fun setData(batch: List<Batch>) {
        val batchDiffUtil = BatchDiffUtil(batchList, batch)
        val batchDiffResult = DiffUtil.calculateDiff(batchDiffUtil)
        this.batchList = batch
        batchDiffResult.dispatchUpdatesTo(this)

    }
}