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
import com.winlay.a3x.components.PremiumCard
import com.winlay.a3x.ui.theme.LocalSpacing
import androidx.compose.foundation.background
import androidx.compose.material.icons.filled.Event
import androidx.compose.ui.draw.clip
import androidx.compose.material3.ExperimentalMaterial3Api

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventScreen(navController: NavController) {
    val scope = rememberCoroutineScope()
    val today = LocalDate.now()
    var events by remember { mutableStateOf<List<Event>>(emptyList()) }
    var selectedDate by remember { mutableStateOf(today) }
    var selectedEvents by remember { mutableStateOf<List<Event>>(emptyList()) }
    var currentMonth by remember { mutableStateOf(YearMonth.now()) }
    val daysOfWeek = DayOfWeek.values()
    val spacing = LocalSpacing.current

    LaunchedEffect(Unit) {
        scope.launch(Dispatchers.IO) {
            try {
                val jsonString = URL("https://winlayassets.a3x.xyz/json/events.json").readText()
                val root = Json.parseToJsonElement(jsonString).jsonObject
                val parsedEvents = Json.decodeFromString<List<Event>>(root["events"].toString())
                events = parsedEvents
                selectedEvents = parsedEvents.filter { it.date == selectedDate.toString() }
            } catch (e: Exception) {
                Log.e("EventScreen", "Failed to fetch events", e)
            }
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Events Calendar",
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.size(40.dp)
                    ) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(spacing.medium)
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
        ) {
            PremiumCalendarHeader(
                currentMonth = currentMonth,
                onPreviousMonth = { currentMonth = currentMonth.minusMonths(1) },
                onNextMonth = { currentMonth = currentMonth.plusMonths(1) }
            )

            Spacer(modifier = Modifier.height(spacing.medium))

            DaysOfWeekHeader(daysOfWeek)

            Spacer(modifier = Modifier.height(spacing.small))

            PremiumCalendarGrid(
                currentMonth = currentMonth,
                selectedDate = selectedDate,
                events = events,
                onDateSelected = { date ->
                    selectedDate = date
                    selectedEvents = events.filter { it.date == date.toString() }
                }
            )

            Spacer(modifier = Modifier.height(spacing.large))

            Text(
                "Events on ${selectedDate}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(bottom = spacing.small)
            )

            if (selectedEvents.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = spacing.xlarge),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(spacing.small)
                    ) {
                        Icon(
                            Icons.Default.Event,
                            contentDescription = "No events",
                            modifier = Modifier.size(32.dp),
                            tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )
                        Text(
                            "No events scheduled",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(spacing.medium)) {
                    selectedEvents.forEach { event ->
                        PremiumEventCard(event)
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumCalendarHeader(
    currentMonth: YearMonth,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    val spacing = LocalSpacing.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextButton(
            onClick = onPreviousMonth,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("‹ ${currentMonth.minusMonths(1).month.getDisplayName(TextStyle.SHORT, Locale.getDefault())}")
        }

        Text(
            text = "${currentMonth.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${currentMonth.year}",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onSurface
        )

        TextButton(
            onClick = onNextMonth,
            colors = ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("${currentMonth.plusMonths(1).month.getDisplayName(TextStyle.SHORT, Locale.getDefault())} ›")
        }
    }
}

@Composable
fun DaysOfWeekHeader(daysOfWeek: Array<DayOfWeek>) {
    val spacing = LocalSpacing.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        daysOfWeek.forEach {
            Text(
                text = it.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
fun PremiumCalendarGrid(
    currentMonth: YearMonth,
    selectedDate: LocalDate,
    events: List<Event>,
    onDateSelected: (LocalDate) -> Unit
) {
    val spacing = LocalSpacing.current
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
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .background(
                                    if (isSelected) MaterialTheme.colorScheme.primary.copy(0.2f)
                                    else Color.Transparent,
                                    CircleShape
                                )
                                .clickable { onDateSelected(date) }
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    day.toString(),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary
                                    else MaterialTheme.colorScheme.onSurface
                                )
                                if (hasEvent) {
                                    Box(
                                        modifier = Modifier
                                            .size(4.dp)
                                            .background(MaterialTheme.colorScheme.primary, CircleShape)
                                    )
                                }
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
fun PremiumEventCard(event: Event) {
    val spacing = LocalSpacing.current

    PremiumCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(spacing.medium)
        ) {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(event.image)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(MaterialTheme.shapes.medium)
            )

            Spacer(modifier = Modifier.height(spacing.medium))

            Text(
                event.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(spacing.small))

            Text(
                event.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(spacing.small))

            Text(
                "Date: ${event.date}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}