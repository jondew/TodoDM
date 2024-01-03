package com.margotjonathan.todo.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.margotjonathan.todo.R
import com.margotjonathan.todo.databinding.FragmentTaskListBinding
import com.margotjonathan.todo.databinding.ItemTaskBinding
import java.util.UUID

object MyTasksDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areItemsTheSame(oldTask: Task, newTask: Task) : Boolean {
        return newTask.id == oldTask.id;
    }

    override fun areContentsTheSame(oldTask: Task, newTask: Task) : Boolean {
        return newTask.title == oldTask.title && newTask.description == oldTask.description;
    }
}

class TaskListAdapter(val listener: TaskListListener) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(MyTasksDiffCallback) {

    // on utilise `inner` ici afin d'avoir accès aux propriétés de l'adapter directement
    inner class TaskViewHolder(binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        private val textView = binding.taskTitle
        private val descriptionTextView = binding.taskDescription
        private val deleteTaskButton = binding.deleteTaskButton
        private val editTaskButton = binding.editTaskButton
        private val wholeTaskView = binding.root

        fun bind(task: Task) {
            textView.text = task.title
            descriptionTextView.text = task.description
            deleteTaskButton.setOnClickListener {
                listener.onClickDelete(task)
            }
            editTaskButton.setOnClickListener {
                listener.onClickEdit(task)
            }
            wholeTaskView.setOnLongClickListener {
                listener.onLongClickTask(task)
                true
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val itemTaskBinding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TaskViewHolder(itemTaskBinding)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}