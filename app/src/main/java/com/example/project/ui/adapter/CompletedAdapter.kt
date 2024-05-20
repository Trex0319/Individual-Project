package com.example.project.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.data.model.Task
import com.example.project.databinding.LayoutCompletedItemBinding

class CompletedAdapter(private var tasks: List<Task>
) : RecyclerView.Adapter<CompletedAdapter.CompletedWorkHolder>()  {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CompletedWorkHolder {
        val binding = LayoutCompletedItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return CompletedWorkHolder(binding)
    }

    override fun getItemCount() = tasks.size

    override fun onBindViewHolder(holder: CompletedWorkHolder, position: Int) {
        val task = tasks[position]
        holder.bind(task)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTasks(tasks: List<Task>) {
        this.tasks = tasks
        notifyDataSetChanged()
    }

    inner class CompletedWorkHolder(
        private val binding: LayoutCompletedItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.task = task
            binding.cvWorks.setOnClickListener {
                listener?.onClick(task)
            }
        }
    }

    interface Listener {
        fun onClick(task: Task)
    }
}