package com.winlay.a3x

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import kotlinx.coroutines.launch
import com.winlay.a3x.components.PremiumCard
import com.winlay.a3x.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppStoreScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    var apps by remember { mutableStateOf<List<App>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var isRefreshing by remember { mutableStateOf(false) }
    var showSearch by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    val spacing = LocalSpacing.current

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
            CenterAlignedTopAppBar(
                title = {
                    Text("App Store", style = MaterialTheme.typography.headlineMedium)
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                ),
                actions = {
                    IconButton(
                        onClick = { showSearch = !showSearch },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary
                        )
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
                    placeholder = { Text("Discover amazing apps...") },
                    leadingIcon = {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                    },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.medium),
                    shape = RoundedCornerShape(spacing.medium)
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
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    apps.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(spacing.medium)
                            ) {
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "No apps",
                                    modifier = Modifier.size(48.dp),
                                    tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                )
                                Text(
                                    "No apps available",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                )
                            }
                        }
                    }

                    else -> {
                        val filteredApps = if (searchQuery.isNotEmpty()) {
                            apps.filter {
                                it.name.contains(searchQuery, ignoreCase = true) ||
                                        it.description.contains(searchQuery, ignoreCase = true)
                            }
                        } else apps

                        if (filteredApps.isEmpty()) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.spacedBy(spacing.medium)
                                ) {
                                    Icon(
                                        Icons.Default.Search,
                                        contentDescription = "No results",
                                        modifier = Modifier.size(48.dp),
                                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                    Text(
                                        "No results for \"$searchQuery\"",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(spacing.medium),
                                verticalArrangement = Arrangement.spacedBy(spacing.medium)
                            ) {
                                items(filteredApps) { app ->
                                    PremiumAppCard(app, navController)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumAppCard(app: App, navController: NavController) {
    val spacing = LocalSpacing.current

    PremiumCard(
        onClick = { navController.navigate("appdetail/${app.id}") },
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.medium),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(app.logoUrl),
                contentDescription = app.name,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(spacing.medium))
            )

            Spacer(modifier = Modifier.width(spacing.medium))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    app.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    app.description.take(80) + if (app.description.length > 80) "..." else "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    maxLines = 2
                )
            }

            Spacer(modifier = Modifier.width(spacing.small))

            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "View details",
                tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f),
                modifier = Modifier.size(20.dp)
            )
        }
    }
}