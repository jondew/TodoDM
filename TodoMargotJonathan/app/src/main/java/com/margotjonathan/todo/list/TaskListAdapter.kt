package com.margotjonathan.todo.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.margotjonathan.todo.R

object MyTasksDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldTask: Task, newTask: Task) : Boolean {
        return newTask.id == oldTask.id;
    }

    override fun areContentsTheSame(oldTask: Task, newTask: Task) : Boolean {
        return newTask.title == oldTask.title && newTask.description == oldTask.description;
    }
}

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(MyTasksDiffCallback) {

    // on utilise `inner` ici afin d'avoir accès aux propriétés de l'adapter directement
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView = itemView.findViewById<TextView>(R.id.task_title)
        private val descriptionTextView = itemView.findViewById<TextView>(R.id.task_description)
        fun bind(task: Task) {
            textView.text = task.title
            descriptionTextView.text = task.description
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}