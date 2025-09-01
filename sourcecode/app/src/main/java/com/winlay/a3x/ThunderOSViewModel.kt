package com.winlay.a3x.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import java.net.URL

@Serializable
data class ThunderOS(
    val name: String,
    val downloadUrl: String
)

class ThunderOSViewModel : ViewModel() {

    private val _thunderOSList = MutableStateFlow<List<ThunderOS>>(emptyList())
    val thunderOSList: StateFlow<List<ThunderOS>> = _thunderOSList

    init {
        fetchThunderOS()
    }

    private fun fetchThunderOS() {
        viewModelScope.launch {
            try {
                val jsonUrl = "https://raw.githubusercontent.com/a3x-xyz/Winlay/refs/heads/main/json/thunderos.json"
                val jsonString = URL(jsonUrl).readText()

                val parsedList = Json.decodeFromString<List<ThunderOS>>(jsonString)

                _thunderOSList.value = parsedList
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
