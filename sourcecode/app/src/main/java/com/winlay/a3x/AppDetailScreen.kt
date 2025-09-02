package com.winlay.a3x

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailScreen(appId: String, navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var app by remember { mutableStateOf<App?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var selectedImage by remember { mutableStateOf<String?>(null) }
    
    LaunchedEffect(appId) {
        val apps = AppRepository.getApps()
        app = apps.find { it.id == appId }
        isLoading = false
    }

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (app == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("App not found.")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.popBackStack() }) {
                Text("Back")
            }
        }
        return
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(app!!.name) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Button(
                onClick = {
                    val intent = CustomTabsIntent.Builder().build()
                    intent.launchUrl(context, Uri.parse(app!!.downloadUrl))
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Download")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = rememberAsyncImagePainter(app!!.logoUrl),
                contentDescription = app!!.name,
                modifier = Modifier
                    .size(96.dp)
                    .padding(8.dp)
                    .clickable { selectedImage = app!!.logoUrl },
                contentScale = ContentScale.Crop
            )

            Text(
                text = app!!.name,
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(top = 8.dp)
            )

            var expanded by remember { mutableStateOf(false) }
            val shortDescription = app!!.description.take(200)
            val isLong = app!!.description.length > 200

            Text(
                text = if (expanded || !isLong) app!!.description else "$shortDescription...",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(top = 16.dp)
            )

            if (isLong) {
                TextButton(onClick = { expanded = !expanded }) {
                    Text(if (expanded) "Less" else "More")
                }
            }

            if (app!!.screenshots.isNotEmpty()) {
                Text(
                    text = "Screenshots",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(top = 24.dp, bottom = 8.dp)
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(app!!.screenshots) { screenshot ->
                        Image(
                            painter = rememberAsyncImagePainter(screenshot),
                            contentDescription = null,
                            modifier = Modifier
                                .height(200.dp)
                                .aspectRatio(9f / 16f)
                                .clickable { selectedImage = screenshot },
                            contentScale = ContentScale.Crop
                        )
                    }
                }
            }
        }

        selectedImage?.let { imageUrl ->
            FullScreenImageViewer(imageUrl) {
                selectedImage = null
            }
        }
    }
}

@Composable
fun FullScreenImageViewer(imageUrl: String, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            )

            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }
    }
}
