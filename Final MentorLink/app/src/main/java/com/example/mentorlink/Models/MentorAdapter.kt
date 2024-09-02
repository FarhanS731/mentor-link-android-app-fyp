package com.example.yourapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.mentorlink.databinding.ItemMentorBinding
import com.example.yourapp.models.Mentor

class MentorAdapter(private var mentors: List<Mentor>) : RecyclerView.Adapter<MentorAdapter.MentorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MentorViewHolder {
        val binding = ItemMentorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MentorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MentorViewHolder, position: Int) {
        val mentor = mentors[position]
        holder.bind(mentor)
    }

    override fun getItemCount(): Int = mentors.size

    fun updateMentors(newMentors: List<Mentor>) {
        mentors = newMentors
        notifyDataSetChanged()
    }

    inner class MentorViewHolder(private val binding: ItemMentorBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mentor: Mentor) {
            binding.mentorName.text = mentor.name ?: "No Name"
            binding.mentorDescription.text = mentor.description ?: "No Description"
            // Bind more views as needed
        }
    }
}
