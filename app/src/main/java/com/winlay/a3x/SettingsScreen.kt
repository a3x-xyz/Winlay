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

@Composable
fun SettingsScreen(viewModel: ThemeViewModel = viewModel()) {
    val currentTheme by viewModel.theme.collectAsState()
    var showThemeDialog by remember { mutableStateOf(false) }
    var showAboutDialog by remember { mutableStateOf(false) }
    var showWhatsNewDialog by remember { mutableStateOf(false) } // NEW
    val uriHandler = LocalUriHandler.current
    var versionTapCount by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineMedium)

        ListItem(
            headlineContent = { Text("Theme") },
            supportingContent = { Text(currentTheme.displayName) },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showThemeDialog = true }
        )

        ListItem(
            headlineContent = { Text("What's new") },
            supportingContent = { Text("Latest changes in this version") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showWhatsNewDialog = true }
        )

        ListItem(
            headlineContent = { Text("About") },
            supportingContent = { Text("App info and credits") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showAboutDialog = true }
        )

        ListItem(
            headlineContent = { Text("Donate") },
            supportingContent = { Text("Support Winlay development") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    uriHandler.openUri("https://a3x.xyz/donate")
                }
        )
    }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Choose Theme") },
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
                                .padding(vertical = 8.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            RadioButton(
                                selected = currentTheme == option,
                                onClick = {
                                    viewModel.setTheme(option)
                                    showThemeDialog = false
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(option.displayName)
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showThemeDialog = false }) {
                    Text("Cancel")
                }
            }
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
                        "1.7",
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

                    Text("License:", style = MaterialTheme.typography.labelMedium)
                    Text(
                        text = "GNU General Public License v3.0",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.clickable {
                            uriHandler.openUri("https://www.gnu.org/licenses/gpl-3.0.html")
                        }
                    )
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
            title = { Text("What's new in 1.7") },
            text = {
                Column {
                    Text("• Changed icon", style = MaterialTheme.typography.bodyMedium)
                    Text("• Fixed bugs", style = MaterialTheme.typography.bodyMedium)
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
