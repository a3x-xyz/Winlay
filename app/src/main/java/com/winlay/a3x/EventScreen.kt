package com.winlay.a3x

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import java.net.URL
import java.time.*
import java.time.format.TextStyle
import java.util.*

@Serializable
data class Event(
    val title: String,
    val description: String,
    val date: String,
    val image: String
)

@Composable
fun EventScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val today = LocalDate.now()
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var selectedDate by remember { mutableStateOf(today) }
    var selectedEvents by remember { mutableStateOf<List<Event>>(emptyList()) }

    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysOfWeek = DayOfWeek.values()

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            try {
                val jsonString = URL("https://raw.githubusercontent.com/a3x-xyz/Winlay/refs/heads/main/json/events.json").readText()
                val root = Json.parseToJsonElement(jsonString).jsonObject
                val parsedEvents = Json.decodeFromString<List<Event>>(root["events"].toString())
                events = parsedEvents
                selectedEvents = parsedEvents.filter { it.date == selectedDate.toString() }
            } catch (e: Exception) {
                Log.e("EventScreen", "Failed to fetch events", e)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text("Events", style = MaterialTheme.typography.headlineSmall)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = { currentMonth = currentMonth.minusMonths(1) }) {
                Text("< ${currentMonth.minusMonths(1).month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}")
            }
            Text(
                text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
                style = MaterialTheme.typography.titleMedium
            )
            TextButton(onClick = { currentMonth = currentMonth.plusMonths(1) }) {
                Text("${currentMonth.plusMonths(1).month.getDisplayName(TextStyle.SHORT, Locale.getDefault())} >")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            daysOfWeek.forEach {
                Text(
                    text = it.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        }

        val firstDayOfMonth = currentMonth.atDay(1)
        val dayOfWeekOffset = firstDayOfMonth.dayOfWeek.ordinal

        val daysInMonth = currentMonth.lengthOfMonth()
        val totalGridCells = dayOfWeekOffset + daysInMonth
        val weeks = (totalGridCells + 6) / 7

        Column {
            for (week in 0 until weeks) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    for (dayOfWeek in 0..6) {
                        val dayIndex = week * 7 + dayOfWeek
                        val day = dayIndex - dayOfWeekOffset + 1

                        if (day in 1..daysInMonth) {
                            val date = currentMonth.atDay(day)
                            val isSelected = selectedDate == date
                            val hasEvent = events.any { it.date == date.toString() }

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .padding(4.dp)
                                    .size(48.dp)
                                    .background(
                                        if (isSelected) MaterialTheme.colorScheme.primary.copy(0.2f) else Color.Transparent,
                                        CircleShape
                                    )
                                    .clickable {
                                        selectedDate = date
                                        selectedEvents = events.filter { it.date == date.toString() }
                                    }
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(day.toString(), style = MaterialTheme.typography.labelMedium)
                                    if (hasEvent) {
                                        Box(
                                            modifier = Modifier
                                                .padding(top = 4.dp)
                                                .size(6.dp)
                                                .background(MaterialTheme.colorScheme.primary, CircleShape)
                                        )
                                    }
                                }
                            }
                        } else {
                            Spacer(modifier = Modifier.size(48.dp))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedEvents.isEmpty()) {
            Text("No events on $selectedDate.", style = MaterialTheme.typography.bodyMedium)
        } else {
            selectedEvents.forEach { event ->
                EventCard(event)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun EventCard(event: Event) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current).data(event.image).crossfade(true).build()
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(event.title, style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(4.dp))
            Text(event.description, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
