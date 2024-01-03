package com.margotjonathan.todo.list

interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
    fun onLongClickTask(task: Task)
}