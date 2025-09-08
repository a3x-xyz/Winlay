package com.winlay.a3x

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import android.util.Base64
import com.winlay.a3x.components.PremiumCard
import com.winlay.a3x.ui.theme.LocalSpacing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Palette

@Composable
fun SettingsScreen(viewModel: ThemeViewModel = viewModel()) {
    val currentTheme by viewModel.theme.collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showWhatsNewDialog by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    var versionTapCount by remember { mutableStateOf(0) }
    val spacing = LocalSpacing.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(spacing.medium),
        verticalArrangement = Arrangement.spacedBy(spacing.medium)
    ) {
        Text(
            "Settings",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        PremiumCard(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(spacing.medium)
            ) {
                Text(
                    "Appearance",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = spacing.small)
                )

                ListItem(
                    headlineContent = {
                        Text(
                            "Theme",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    supportingContent = {
                        Text(
                            currentTheme.displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showThemeDialog = true },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = "Theme",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
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
                    "About & Information",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = spacing.small)
                )

                ListItem(
                    headlineContent = {
                        Text(
                            "What's New",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    supportingContent = {
                        Text(
                            "Latest changes and updates",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showWhatsNewDialog = true },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "What's New",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )

                ListItem(
                    headlineContent = {
                        Text(
                            "About Winlay",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    supportingContent = {
                        Text(
                            "App info and credits",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showAboutDialog = true },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Business,
                            contentDescription = "About",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
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
                    "Support & Contribution",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = spacing.small)
                )

                ListItem(
                    headlineContent = {
                        Text(
                            "Donate",
                            style = MaterialTheme.typography.bodyLarge
                        )
                    },
                    supportingContent = {
                        Text(
                            "Support A3X development",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            uriHandler.openUri("https://a3x.xyz/donate")
                        },
                    leadingContent = {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Donate",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                )
            }
        }
    }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = {
                Text(
                    "Choose Theme",
                    style = MaterialTheme.typography.titleLarge
                )
            },
            text = {
                Column {
                    ThemeOption.values().forEach { option ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setTheme(option)
                                    showThemeDialog = false
                                }
                                .padding(vertical = spacing.small),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            RadioButton(
                                selected = currentTheme == option,
                                onClick = {
                                    viewModel.setTheme(option)
                                    showThemeDialog = false
                                },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = MaterialTheme.colorScheme.primary
                                )
                            )
                            Spacer(Modifier.width(spacing.small))
                            Text(
                                option.displayName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(
                    onClick = { showThemeDialog = false },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Cancel")
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface,
            textContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
        )
    }

    if (showAboutDialog) {
        AlertDialog(
            onDismissRequest = {
                showAboutDialog = false
                versionTapCount = 0
            },
            title = { Text("About Winlay") },
            text = {
                Column {
                    Text("App name:", style = MaterialTheme.typography.labelMedium)
                    Text("Winlay", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Version:", style = MaterialTheme.typography.labelMedium)
                    Text(
                        "v2.0",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable {
                            versionTapCount++
                            if (versionTapCount >= 8) {
                                try {
                                    val rickrollUrl = "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
                                    uriHandler.openUri(rickrollUrl)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                } finally {
                                    versionTapCount = 0
                                }
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Developer by:", style = MaterialTheme.typography.labelMedium)
                    Text("A3X", style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("A3X Website:", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = "https://a3x.xyz/",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable {
                            uriHandler.openUri("https://a3x.xyz/")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Winlay Website:", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = "https://winlayapp.weebly.com/",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable {
                            uriHandler.openUri("https://winlayapp.weebly.com/")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("Github:", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = "https://github.com/a3x-xyz/Winlay",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable {
                            uriHandler.openUri("https://github.com/a3x-xyz/Winlay")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text("License:", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = "GNU General Public License v3.0",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable {
                            uriHandler.openUri("https://www.gnu.org/licenses/gpl-3.0.html")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    showAboutDialog = false
                    versionTapCount = 0
                }) {
                    Text("OK")
                }
            }
        )
    }

    if (showWhatsNewDialog) {
        AlertDialog(
            onDismissRequest = { showWhatsNewDialog = false },
            title = { Text("What's new in v2.0") },
            text = {
                Column {
                    Text("• Redesigned UI", style = MaterialTheme.typography.bodyMedium)
                    Text("• Updated icons", style = MaterialTheme.typography.bodyMedium)
                    Text("• Version numbers now start with 'v'", style = MaterialTheme.typography.bodyMedium)
                    Text("• Bug fixes", style = MaterialTheme.typography.bodyMedium)
                    Text("• Changed JSON and image URLs to avoid rate limits.", style = MaterialTheme.typography.bodyMedium)
                }
            },
            confirmButton = {
                TextButton(onClick = { showWhatsNewDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}
