package com.example.mentorlink.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mentorlink.R
import com.example.mentorlink.Student

class StudentListAdapter(
    private val context: Context,
    private var studentList: List<Student>
) : RecyclerView.Adapter<StudentListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.student_list_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val student = studentList.getOrNull(position) // Ensure null safety
        student?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int = studentList.size

    fun updateData(newStudentList: List<Student>) {
        studentList = newStudentList
        notifyDataSetChanged() // Consider using more specific notify methods
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView? = itemView.findViewById(R.id.nameTextView)
        private val emailTextView: TextView? = itemView.findViewById(R.id.emailTextView)

        fun bind(student: Student) {
            // Check if TextView references are not null before accessing them
            nameTextView?.text = student.name
            emailTextView?.text = student.email
        }
    }
}
