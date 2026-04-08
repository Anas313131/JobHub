package com.example.jobhub

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.jobhub.ui.theme.JobHubTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            JobHubTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    JobLinksScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

data class JobPlatform(val name: String)

@Composable
fun JobLinksScreen(modifier: Modifier = Modifier) {
    val platforms = listOf(
        JobPlatform("LinkedIn"),
        JobPlatform("Naukri"),
        JobPlatform("Indeed"),
        JobPlatform("Instahyre"),
        JobPlatform("Wellfound")
    )

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Job Portal Links",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(platforms) { platform ->
                JobLinkItem(platform = platform)
            }
        }
    }
}

@Composable
fun JobLinkItem(platform: JobPlatform) {
    var url by rememberSaveable { mutableStateOf("") }
    val uriHandler = LocalUriHandler.current
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = platform.name, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(4.dp))
        OutlinedTextField(
            value = url,
            onValueChange = { url = it },
            label = { Text("Enter ${platform.name} URL") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            Button(onClick = {
                // Here you would typically save the URL to DataStore or SharedPreferences
                Toast.makeText(context, "${platform.name} URL Saved", Toast.LENGTH_SHORT).show()
            }) {
                Text("Save")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = {
                if (url.isNotBlank()) {
                    try {
                        // Ensure the URL has a protocol
                        val formattedUrl = if (!url.startsWith("http://") && !url.startsWith("https://")) {
                            "https://$url"
                        } else {
                            url
                        }
                        uriHandler.openUri(formattedUrl)
                    } catch (e: Exception) {
                        Toast.makeText(context, "Invalid URL", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Please enter a URL", Toast.LENGTH_SHORT).show()
                }
            }) {
                Text("Open")
            }
        }
    }
}
