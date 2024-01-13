package com.margotjonathan.todo.data

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.margotjonathan.todo.list.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class TasksListViewModel : ViewModel() {
    private val webService = Api.tasksWebService

    public val tasksStateFlow = MutableStateFlow<List<Task>>(emptyList())

    fun refresh() {
        viewModelScope.launch {
            val response = webService.fetchTasks() // Call HTTP (opération longue)
            if (!response.isSuccessful) { // à cette ligne, on a reçu la réponse de l'API
                Log.e("Network", "Error: ${response.message()}")
                return@launch
            }
            val fetchedTasks = response.body()!!
            tasksStateFlow.value = fetchedTasks // on modifie le flow, ce qui déclenche ses observers
        }
    }

    // à compléter plus tard:
    fun add(task: Task) {
        viewModelScope.launch {
            val response = webService.create(task)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }

            val createdTask = response.body()!!
            val updatedList = tasksStateFlow.value + createdTask
            tasksStateFlow.value = updatedList
        }
    }
    fun edit(task: Task) {
        viewModelScope.launch {
            val response = webService.update(task)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }

            val updatedTask = response.body()!!
            val updatedList = tasksStateFlow.value.map {
                if (it.id == updatedTask.id) updatedTask else it
            }
            tasksStateFlow.value = updatedList
        }
    }
    fun remove(task: Task) {
        viewModelScope.launch {
            val response = webService.delete(task.id)
            if (!response.isSuccessful) {
                Log.e("Network", "Error: ${response.raw()}")
                return@launch
            }

            //la task supprimée étant "task"
            val updatedList = tasksStateFlow.value - task
            tasksStateFlow.value = updatedList
        }
    }
}