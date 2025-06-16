package com.winlay.a3x

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.winlay.a3x.ui.theme.WinlayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val viewModel: ThemeViewModel = viewModel()
            val theme by viewModel.theme.collectAsState()

            val isDarkTheme = when (theme) {
                ThemeOption.SYSTEM -> isSystemInDarkTheme()
                ThemeOption.DARK -> true
                ThemeOption.LIGHT -> false
            }

            val view = LocalView.current
            val context = LocalContext.current
            var isConnected by remember { mutableStateOf(true) }

            LaunchedEffect(Unit) {
                isConnected = isInternetAvailable(context)
            }

            SideEffect {
                val window = this@MainActivity.window
                WindowCompat.setDecorFitsSystemWindows(window, false)
                WindowCompat.getInsetsController(window, view)
                    .isAppearanceLightStatusBars = !isDarkTheme
                window.statusBarColor = Color.Transparent.value.toInt()
            }

            WinlayTheme(darkTheme = isDarkTheme) {
                MainScreen()
                if (!isConnected) {
                    AlertDialog(
                        onDismissRequest = { /* Prevent dismiss on tap outside */ },
                        confirmButton = {
                            TextButton(onClick = {
                                isConnected = isInternetAvailable(context)
                            }) {
                                Text("Retry")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                (context as? Activity)?.finish()
                            }) {
                                Text("Exit")
                            }
                        },
                        title = {
                            Text("Internet Required")
                        },
                        text = {
                            Text("Please connect to the internet to continue using the app.")
                        }
                    )
                }
            }
        }
    }
}
