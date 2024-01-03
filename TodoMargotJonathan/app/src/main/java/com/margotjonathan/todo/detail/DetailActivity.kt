package com.margotjonathan.todo.detail

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.margotjonathan.todo.detail.ui.theme.TodoMargotJonathanTheme
import com.margotjonathan.todo.list.Task
import java.util.UUID

class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val onValidate: (Task) -> Unit = { newTask ->
            intent.putExtra("task", newTask)
            setResult(RESULT_OK, intent)
            finish()
        }
        setContent {
            TodoMargotJonathanTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Detail(onValidate)
                }
            }
        }
    }
}

@Composable
fun Detail(onValidate: (Task) -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    )
    {
        Text(
            text = "Task Details",
            modifier = modifier,
            style = MaterialTheme.typography.headlineLarge
        )
        var task by remember { mutableStateOf(Task(id = UUID.randomUUID().toString(), title = "", description = "")) }
        OutlinedTextField(
            value = task.title,
            onValueChange = { task = task.copy(title = it) },
            label = { Text("Title") }
        )
        OutlinedTextField(
            value = task.description,
            onValueChange = { task = task.copy(description = it) },
            label = { Text("Description") }
        )
        Button(onClick = {
            onValidate(task)
        })
        {
            Text(text = "Save")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    TodoMargotJonathanTheme {
        Detail(onValidate = {})
    }
}