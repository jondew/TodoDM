package com.margotjonathan.todo.user

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.AsyncImage
import com.margotjonathan.todo.data.Api
import com.margotjonathan.todo.data.UserWebService
import com.margotjonathan.todo.user.ui.theme.TodoMargotJonathanTheme
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UserActivity : ComponentActivity() {
    private fun Bitmap.toRequestBody(): MultipartBody.Part {
        val tmpFile = File.createTempFile("avatar", "jpg")
        tmpFile.outputStream().use { // *use*: open et close automatiquement
            this.compress(Bitmap.CompressFormat.JPEG, 100, it) // *this* est le bitmap ici
        }
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "avatar.jpg",
            body = tmpFile.readBytes().toRequestBody()
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var bitmap: Bitmap? by remember { mutableStateOf(null) }
            var uri: Uri? by remember { mutableStateOf(null) }
            val composeScope = rememberCoroutineScope()
            val takePicture = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) {
                bitmap = it
                composeScope.launch {
                    Api.userWebService.updateAvatar(bitmap?.toRequestBody())
                }
            }
            Column {
                AsyncImage(
                    modifier = Modifier.fillMaxHeight(.2f),
                    model = bitmap ?: uri,
                    contentDescription = null
                )
                Button(
                    onClick = {
                        takePicture.launch()
                    },
                    content = { Text("Take picture") }
                )
                Button(
                    onClick = {},
                    content = { Text("Pick photo") }
                )
            }
        }
    }
}

@Composable
fun User(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun UserPreview() {
    TodoMargotJonathanTheme {
        User("Android")
    }
}