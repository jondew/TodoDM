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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.margotjonathan.todo.detail.ui.theme.TodoMargotJonathanTheme

class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TodoMargotJonathanTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Detail()
                }
            }
        }
    }
}

@Composable
fun Detail(modifier: Modifier = Modifier) {
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
        Text(
            text = "title"
        )
        Text(
            text = "description"
        )
        Button(onClick = { /*TODO*/ }) {
            Text(text = "Save")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    TodoMargotJonathanTheme {
        Detail()
    }
}