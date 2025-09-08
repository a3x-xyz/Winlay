package com.winlay.a3x

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.Window
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.winlay.a3x.components.PremiumCard
import com.winlay.a3x.ui.theme.LocalSpacing

@Composable
fun HomeScreen(navController: NavController) {
    val uriHandler = LocalUriHandler.current
    val spacing = LocalSpacing.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.medium)
    ) {
        Text(
            "Home",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = spacing.large)
        )

        PremiumCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacing.large)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.medium)
            ) {
                Text(
                    "Download",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = spacing.medium)
                )

                DownloadItem(icon = Icons.Filled.Computer, label = "Linux") {
                    navController.navigate("detail/Linux")
                }
                DownloadItem(icon = Icons.Filled.Android, label = "Android") {
                    navController.navigate("detail/Android")
                }
                DownloadItem(icon = Icons.Filled.Window, label = "Windows") {
                    navController.navigate("detail/Windows")
                }
            }
        }

        PremiumCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = spacing.large)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.medium)
            ) {
                Text(
                    "Connect With Us",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = spacing.medium)
                )

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
            }
        }

        PremiumCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.medium)
            ) {
                Text(
                    "Explore More",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = spacing.medium)
                )

                OtherItem(icon = Icons.Filled.Event, label = "Events") {
                    navController.navigate("event")
                }
                OtherItem(icon = Icons.Filled.Computer, label = "Thunder OS") {
                    navController.navigate("thunderos")
                }
            }
        }
    }
}

@Composable
fun DownloadItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    val spacing = LocalSpacing.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacing.small)
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(spacing.medium))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Filled.ArrowForward,
            contentDescription = "Go",
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun SocialItem(resourceId: Int, label: String, onClick: () -> Unit) {
    val spacing = LocalSpacing.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacing.small)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary)
        )
        Spacer(modifier = Modifier.width(spacing.medium))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Filled.ArrowForward,
            contentDescription = "Go",
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
    }
}

@Composable
fun OtherItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    val spacing = LocalSpacing.current

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = spacing.small)
            .clickable { onClick() }
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(spacing.medium))
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.Filled.ArrowForward,
            contentDescription = "Go",
            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
        )
    }
}