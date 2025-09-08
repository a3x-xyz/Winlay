package com.winlay.a3x.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.winlay.a3x.ui.theme.LocalSpacing

@Composable
fun PremiumNavigationBar(
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit
) {
    val spacing = LocalSpacing.current

    NavigationBar(
        modifier = modifier.height(72.dp),
        containerColor = androidx.compose.material3.MaterialTheme.colorScheme.surface,
        contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurface,
        tonalElevation = 8.dp,
        content = content
    )
}

@Composable
fun RowScope.PremiumNavigationBarItem(
    selected: Boolean,
    onClick: () -> Unit,
    icon: @Composable () -> Unit,
    label: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationBarItem(
        selected = selected,
        onClick = onClick,
        icon = icon,
        label = label,
        modifier = modifier,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
            selectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
            unselectedIconColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedTextColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = androidx.compose.material3.MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
        )
    )
}