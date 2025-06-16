package com.winlay.a3x

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Window
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.compose.material.icons.filled.Chat
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.material.icons.filled.Event

@Composable
fun SocialItem(resourceId: Int, label: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun HomeScreen(navController: NavController) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Home", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))

        Text("Download", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        DownloadItem(icon = Icons.Filled.Computer, label = "Linux") {
            navController.navigate("detail/Linux")
        }
        DownloadItem(icon = Icons.Filled.Android, label = "Android") {
            navController.navigate("detail/Android")
        }
        DownloadItem(icon = Icons.Filled.Window, label = "Windows") {
            navController.navigate("detail/Windows")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Social", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        SocialItem(resourceId = R.drawable.ic_discord, label = "Discord") {
            uriHandler.openUri("https://discord.gg/EBvgf8hpvw")
        }
        SocialItem(resourceId = R.drawable.ic_youtube, label = "YouTube") {
            uriHandler.openUri("https://youtube.com/@rimvydoplus")
        }
        SocialItem(resourceId = R.drawable.ic_instagram, label = "Instagram") {
            uriHandler.openUri("https://www.instagram.com/rimvydop")
        }
        SocialItem(resourceId = R.drawable.ic_x, label = "X") {
            uriHandler.openUri("https://x.com/rimvydopmusic")
        }

        Spacer(modifier = Modifier.height(32.dp))

        Text("Other", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        OtherItem(icon = Icons.Filled.Event, label = "Event") {
            navController.navigate("event")
        }
    }
}

@Composable
fun DownloadItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Icon(imageVector = icon, contentDescription = label)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
fun OtherItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable { onClick() }
    ) {
        Icon(imageVector = icon, contentDescription = label)
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, style = MaterialTheme.typography.bodyLarge)
    }
}


