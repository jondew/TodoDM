package com.margotjonathan.todo.data

import com.margotjonathan.todo.list.Task
import retrofit2.Response
import retrofit2.http.GET

interface TasksWebService {
    @GET("/rest/v2/tasks/")
    suspend fun fetchTasks(): Response<List<Task>>
}