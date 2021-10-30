package com.example.feesapplication.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.feesapplication.data.database.entities.Student
import com.example.feesapplication.databinding.StudentRowLayoutBinding


class StudentListAdapter : RecyclerView.Adapter<StudentListAdapter.MyViewHolder>() {

    private var studentList = emptyList<Student>()

    class MyViewHolder(private val binding: StudentRowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(student: Student){
            binding.studentInfo = student
            binding.executePendingBindings()
        }
        companion object{
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

    fun setStudentData(student: List<Student>){
        this.studentList = student
        notifyDataSetChanged()
    }
}