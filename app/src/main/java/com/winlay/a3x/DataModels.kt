package com.winlay.a3x

import kotlinx.serialization.Serializable

@Serializable
data class Product(
    val name: String,
    val description: String,
    val iconUrl: String,
    val downloads: List<DownloadLink>
)

@Serializable
data class DownloadLink(
    val label: String,
    val url: String
)

@Serializable
data class Event(
    val title: String,
    val description: String,
    val date: String,
    val image: String
)