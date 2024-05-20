package com.example.project.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.data.model.Task
import com.example.project.databinding.LayoutTodoItemBinding

class TodoAdapter(
    private var items: List<Task>

) : RecyclerView.Adapter<TodoAdapter.TodoViewHolder>() {

    var listener: Listener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val binding = LayoutTodoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TodoViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setTasks(items: List<Task>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class TodoViewHolder(
        private val binding: LayoutTodoItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(task: Task) {
            binding.task = task
            binding.mcItem.setOnClickListener{
                listener?.onClick(task)
            }
        }
    }

    interface Listener {
        fun onClick(task: Task)
    }

}