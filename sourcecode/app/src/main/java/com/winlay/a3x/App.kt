package com.winlay.a3x

import kotlinx.serialization.Serializable

@Serializable
data class App(
    val id: String,
    val name: String,
    val description: String,
    val logoUrl: String,
    val screenshots: List<String>,
    val downloadUrl: String
)
