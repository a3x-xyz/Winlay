package com.winlay.a3x

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import java.net.URL
import com.winlay.a3x.components.PremiumCard
import com.winlay.a3x.ui.theme.LocalSpacing
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape
import com.winlay.a3x.ui.theme.WinlayError
import androidx.compose.material.icons.filled.Error


@Composable
fun DetailScreen(name: String, navController: NavController) {
    val spacing = LocalSpacing.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.medium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacing.large)
        ) {
            IconButton(
                onClick = { navController.navigateUp() },
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        MaterialTheme.shapes.small
                    )
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.width(spacing.medium))
            Text(
                name,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        when (name) {
            "Windows" -> PremiumProductList("Windows")
            "Linux" -> PremiumProductList("Linux")
            "Android" -> PremiumProductList("Android")
            else -> {
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
                            contentDescription = "Coming soon",
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Text(
                            "Coming soon",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumProductList(type: String) {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var query by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var showSearch by remember { mutableStateOf(false) }
    val spacing = LocalSpacing.current

    LaunchedEffect(type) {
        try {
            val jsonString = withContext(Dispatchers.IO) {
                URL("https://winlayassets.a3x.xyz/json/${type.lowercase()}.json").readText()
            }
            val jsonArray = JSONArray(jsonString)
            products = (0 until jsonArray.length()).map { i ->
                val obj = jsonArray.getJSONObject(i)
                Product(
                    name = obj.getString("name"),
                    description = obj.getString("description"),
                    iconUrl = obj.getString("iconUrl"),
                    downloads = obj.getJSONArray("downloads").let { downloadsArray ->
                        (0 until downloadsArray.length()).map { j ->
                            val d = downloadsArray.getJSONObject(j)
                            DownloadLink(d.getString("label"), d.getString("url"))
                        }
                    }
                )
            }
        } catch (e: Exception) {
            error = "Failed to load products. Please check your connection."
        }
    }

    val filteredProducts = products.filter {
        val trimmedQuery = query.trim()
        it.name.contains(trimmedQuery, ignoreCase = true) ||
                it.description.contains(trimmedQuery, ignoreCase = true)
    }

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacing.medium),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$type Products",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )

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

        if (showSearch) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search $type products...") },
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
                    .padding(bottom = spacing.medium),
                shape = RoundedCornerShape(spacing.medium)
            )
        }

        when {
            error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(spacing.medium)
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = "Error",
                            modifier = Modifier.size(48.dp),
                            tint = WinlayError
                        )
                        Text(
                            error ?: "Error",
                            style = MaterialTheme.typography.bodyLarge,
                            color = WinlayError
                        )
                    }
                }
            }

            products.isEmpty() -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                }
            }

            else -> {
                if (filteredProducts.isEmpty()) {
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
                                "No results found for \"${query.trim()}\"",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(spacing.medium)
                    ) {
                        items(filteredProducts) { product ->
                            PremiumProductCard(product)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumProductCard(product: Product) {
    val context = LocalContext.current
    val spacing = LocalSpacing.current

    PremiumCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.medium)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(product.iconUrl)
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(56.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.width(spacing.medium))

                Text(
                    product.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(spacing.medium))

            var expanded by remember { mutableStateOf(false) }
            val preview = if (product.description.length > 120 && !expanded) {
                product.description.take(120) + "..."
            } else {
                product.description
            }

            Text(
                preview,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            if (product.description.length > 120) {
                TextButton(
                    onClick = { expanded = !expanded },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(if (expanded) "Show Less" else "Show More")
                }
            }

            Spacer(modifier = Modifier.height(spacing.medium))

            Text(
                "Download Options:",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f),
                modifier = Modifier.padding(bottom = spacing.small)
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(spacing.small)
            ) {
                product.downloads.forEach { download ->
                    OutlinedButton(
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(download.url))
                            context.startActivity(intent)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.small,
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text(download.label)
                    }
                }
            }
        }
    }
}