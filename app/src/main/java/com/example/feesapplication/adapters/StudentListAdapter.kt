package com.example.feesapplication.adapters

import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.feesapplication.R
import com.example.feesapplication.data.database.FeeStatus
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.databinding.StudentRowLayoutBinding


class StudentListAdapter() : RecyclerView.Adapter<StudentListAdapter.MyViewHolder>() {

    var studentList = emptyList<Student>()






    class MyViewHolder(private val binding: StudentRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(student: Student) {
            binding.studentInfo = student
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = StudentRowLayoutBinding.inflate(layoutInflater, parent, false)
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
        return studentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val currentStudent = studentList[position]
        holder.bind(currentStudent)


    }



    fun setStudentData(student: List<Student>) {
        val studentDiffUtil = StudentDiffUtil(studentList, student)
        val studentDiffResult = DiffUtil.calculateDiff(studentDiffUtil)
        this.studentList = student

        studentDiffResult.dispatchUpdatesTo(this)
    }


}