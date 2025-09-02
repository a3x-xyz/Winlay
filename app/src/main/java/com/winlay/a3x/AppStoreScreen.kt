package com.winlay.a3x

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppStoreScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    var apps by remember { mutableStateOf<List<App>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    fun loadApps() {
        scope.launch {
            isLoading = true
            try {
                apps = AppRepository.loadAppsFromGitHub()
            } catch (e: Exception) {
                apps = emptyList()
            } finally {
                isLoading = false
                isRefreshing = false
            }
        }
    }

    LaunchedEffect(Unit) {
        loadApps()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("App Store", style = MaterialTheme.typography.headlineMedium)
                },
                actions = {
                    IconButton(onClick = { showSearch = !showSearch }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                }
            )
        }
    ) { innerPadding ->

        Column(modifier = Modifier.padding(innerPadding)) {
            if (showSearch) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("What are you looking for?") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }

            SwipeRefresh(
                state = remember { SwipeRefreshState(isRefreshing) },
                onRefresh = {
                    isRefreshing = true
                    loadApps()
                },
                modifier = Modifier.fillMaxSize()
            ) {
                when {
                    isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    apps.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("Failed to load.")
                        }
                    }

                    else -> {
                        val filteredApps = if (searchQuery.isNotEmpty()) {
                            apps.filter {
                                it.name.contains(searchQuery, ignoreCase = true) ||
                                        it.description.contains(searchQuery, ignoreCase = true)
                            }
                        } else apps

                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(filteredApps) { app ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            navController.navigate("appdetail/${app.id}")
                                        }
                                ) {
                                    Row(modifier = Modifier.padding(16.dp)) {
                                        Image(
                                            painter = rememberAsyncImagePainter(app.logoUrl),
                                            contentDescription = app.name,
                                            modifier = Modifier.size(64.dp)
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Column {
                                            Text(app.name, style = MaterialTheme.typography.titleMedium)
                                            Text(
                                                app.description.take(80) + "...",
                                                style = MaterialTheme.typography.bodySmall
                                            )
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
}