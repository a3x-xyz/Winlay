package com.winlay.a3x

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request

object AppRepository {
    private var cachedApps: List<App>? = null

    suspend fun getApps(): List<App> {
        if (cachedApps == null) {
            cachedApps = loadAppsFromGitHub()
        }
        return cachedApps!!
    }

    suspend fun loadAppsFromGitHub(): List<App> = withContext(Dispatchers.IO) {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://winlayassets.a3x.xyz/json/apps.json")
            .build()

        val response = client.newCall(request).execute()
        val json = response.body?.string()
        Json.decodeFromString(json ?: "[]")
    }

    val apps: List<App>
        get() = cachedApps ?: emptyList()
}