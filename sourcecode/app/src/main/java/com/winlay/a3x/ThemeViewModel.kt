package com.winlay.a3x

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private val THEME_KEY = stringPreferencesKey("theme")

private val Application.dataStore by preferencesDataStore("settings")

class ThemeViewModel(app: Application) : AndroidViewModel(app) {

    private val dataStore = app.dataStore

    val theme: StateFlow<ThemeOption> = dataStore.data
        .map { prefs ->
            when (prefs[THEME_KEY]) {
                ThemeOption.LIGHT.name -> ThemeOption.LIGHT
                ThemeOption.DARK.name -> ThemeOption.DARK
                else -> ThemeOption.SYSTEM
            }
        }
        .stateIn(viewModelScope, SharingStarted.Eagerly, ThemeOption.SYSTEM)

    fun setTheme(option: ThemeOption) {
        viewModelScope.launch {
            dataStore.edit { prefs ->
                prefs[THEME_KEY] = option.name
            }
        }
    }
}
