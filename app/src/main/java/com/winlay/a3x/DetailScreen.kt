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

data class Product(
    val name: String,
    val description: String,
    val iconUrl: String,
    val downloads: List<DownloadLink>
)

data class DownloadLink(val label: String, val url: String)

@Composable
fun DetailScreen(name: String, navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IconButton(
            onClick = { navController.navigateUp() },
            modifier = Modifier.size(40.dp)
        ) {
            Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
        }

        Spacer(modifier = Modifier.height(16.dp))

        when (name) {
            "Windows" -> ProductList("Windows")
            "Linux" -> ProductList("Linux")
            "Android" -> ProductList("Android")
            else -> Text("Coming soon", style = MaterialTheme.typography.bodyLarge)
        }
    }
}

@Composable
fun ProductList(type: String) {
    var products by remember { mutableStateOf<List<Product>>(emptyList()) }
    var query by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    var showSearch by remember { mutableStateOf(false) }

    LaunchedEffect(type) {
        try {
            val jsonString = withContext(Dispatchers.IO) {
                URL("https://raw.githubusercontent.com/a3x-xyz/Winlay/refs/heads/main/json/${type.lowercase()}.json").readText()
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
            error = "Failed to load. Please check your connection."
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
                .padding(bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = type,
                style = MaterialTheme.typography.headlineLarge,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = { showSearch = !showSearch }) {
                Icon(Icons.Default.Search, contentDescription = "Search")
            }
        }

        if (showSearch) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("What are you looking for?") },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            )
        } else {
            Spacer(modifier = Modifier.height(8.dp))
        }

        when {
            error != null -> Text(error ?: "Error", color = MaterialTheme.colorScheme.error)
            products.isEmpty() -> Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
            else -> {
                if (filteredProducts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 32.dp),
                        contentAlignment = Alignment.TopCenter
                    ) {
                        Text(
                            text = "No results found for \"${query.trim()}\"",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                } else {
                    LazyColumn {
                        items(filteredProducts) { product ->
                            ProductCard(product)
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ProductCard(product: Product) {
    val context = LocalContext.current

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(context)
                            .data(product.iconUrl)
                            .crossfade(true)
                            .build()
                    ),
                    contentDescription = product.name,
                    modifier = Modifier
                        .size(48.dp)
                        .padding(end = 8.dp),
                    contentScale = ContentScale.Fit
                )
                Text(product.name, style = MaterialTheme.typography.titleMedium)
            }

            Spacer(modifier = Modifier.height(8.dp))

            var expanded by remember { mutableStateOf(false) }
            val preview = if (product.description.length > 120 && !expanded) {
                product.description.take(120) + "..."
            } else {
                product.description
            }

            Text(preview, style = MaterialTheme.typography.bodyMedium)
            if (product.description.length > 120) {
                Text(
                    text = if (expanded) "Less" else "More",
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable { expanded = !expanded }
                        .padding(vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            product.downloads.forEach { download ->
                Text(
                    text = download.label,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .clickable {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(download.url))
                            context.startActivity(intent)
                        }
                        .padding(vertical = 4.dp)
                )
            }
        }
    }
}
