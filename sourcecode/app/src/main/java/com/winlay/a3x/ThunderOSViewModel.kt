package com.winlay.a3x

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.request.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json


class ThunderOSViewModel : ViewModel() {
    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
    }

    private val _data = MutableStateFlow<ThunderOSData?>(null)
    val data: StateFlow<ThunderOSData?> = _data

    init {
        fetchData()
    }

    private fun fetchData() {
        viewModelScope.launch {
            try {
                val response: String = client.get("https://raw.githubusercontent.com/a3x-xyz/Winlay/main/json/thunderos.json").body()

                val parsed = Json.decodeFromString<ThunderOSData>(response)
                _data.value = parsed
            } catch (e: Exception) {
                e.printStackTrace()
                println("Failed to fetch ThunderOS data: ${e.message}")
            }
        }
    }
}
