package com.winlay.a3x.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import androidx.compose.ui.platform.LocalUriHandler

@Serializable
data class ThunderOSItem(
    val name: String,
    val image: String,
    val download: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThunderOSScreen(navController: NavController) {
    var items by remember { mutableStateOf<List<ThunderOSItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    val client = remember { HttpClient(CIO) }
    DisposableEffect(Unit) {
        onDispose { client.close() }
    }

    LaunchedEffect(Unit) {
        try {
            val jsonText = client
                .get("https://winlayassets.a3x.xyz/json/thunderos.json")
                .bodyAsText()

            val parsed = Json { ignoreUnknownKeys = true }
                .decodeFromString<List<ThunderOSItem>>(jsonText)

            items = parsed
        } catch (e: Exception) {
            e.printStackTrace()
            items = emptyList()
        } finally {
            isLoading = false
        }
    }

    val uriHandler = LocalUriHandler.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thunder OS") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                items.isEmpty() -> {
                    Text(
                        "No Thunder OS items found",
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        items(items) { item ->
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(12.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Image(
                                    painter = rememberAsyncImagePainter(item.image),
                                    contentDescription = item.name,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(200.dp)
                                )

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.weight(1f),
                                        textAlign = TextAlign.Start
                                    )
                                    Button(onClick = { uriHandler.openUri(item.download) }) {
                                        Text("Download")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
