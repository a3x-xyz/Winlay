package com.winlay.a3x

import kotlinx.serialization.Serializable

@Serializable
data class ThunderOSData(
    val name: String,
    val image: String,
    val download: String
)

