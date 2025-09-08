package com.winlay.a3x

import android.net.Uri
import androidx.browser.customtabs.CustomTabsIntent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import kotlinx.coroutines.launch
import com.winlay.a3x.components.PremiumButton
import com.winlay.a3x.ui.theme.LocalSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDetailScreen(appId: String, navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val spacing = LocalSpacing.current

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
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
        return
    }

    if (app == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(spacing.large),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                Icons.Default.Search,
                contentDescription = "Not found",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            )
            Spacer(modifier = Modifier.height(spacing.medium))
            Text("App not found.", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(spacing.medium))
            PremiumButton(
                onClick = { navController.popBackStack() },
                text = "Go Back"
            )
        }
        return
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        app!!.name,
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        bottomBar = {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 4.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(spacing.medium),
                    horizontalArrangement = Arrangement.Center
                ) {
                    PremiumButton(
                        onClick = {
                            val intent = CustomTabsIntent.Builder().build()
                            intent.launchUrl(context, Uri.parse(app!!.downloadUrl))
                        },
                        text = "Download",
                        modifier = Modifier.fillMaxWidth(0.8f)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(spacing.xlarge),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = rememberAsyncImagePainter(app!!.logoUrl),
                    contentDescription = app!!.name,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(32.dp))
                        .clickable { selectedImage = app!!.logoUrl },
                    contentScale = ContentScale.Crop
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.medium),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = app!!.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(spacing.medium))

                var expanded by remember { mutableStateOf(false) }
                val shortDescription = app!!.description.take(200)
                val isLong = app!!.description.length > 200

                Text(
                    text = if (expanded || !isLong) app!!.description else "$shortDescription...",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
                )

                if (isLong) {
                    TextButton(
                        onClick = { expanded = !expanded },
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(if (expanded) "Read Less" else "Read More")
                    }
                }

                if (app!!.screenshots.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(spacing.large))

                    Text(
                        text = "Screenshots",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Spacer(modifier = Modifier.height(spacing.small))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(spacing.medium),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(app!!.screenshots) { screenshot ->
                            Image(
                                painter = rememberAsyncImagePainter(screenshot),
                                contentDescription = null,
                                modifier = Modifier
                                    .height(200.dp)
                                    .aspectRatio(9f / 16f)
                                    .clip(RoundedCornerShape(spacing.medium))
                                    .clickable { selectedImage = screenshot },
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                }
            }
        }

        selectedImage?.let { imageUrl ->
            androidx.compose.ui.window.Dialog(onDismissRequest = { selectedImage = null }) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.9f)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(imageUrl),
                        contentDescription = null,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.8f)
                    )

                    IconButton(
                        onClick = { selectedImage = null },
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(20.dp)
                            .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            .size(40.dp)
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
    }
}