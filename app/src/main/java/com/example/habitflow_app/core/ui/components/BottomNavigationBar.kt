package com.example.habitflow_app.core.ui.components

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.habitflow_app.R
import com.example.habitflow_app.core.ui.theme.AppTypography
import com.example.habitflow_app.core.ui.theme.HabitflowAppTheme
import com.example.habitflow_app.navigation.NavDestinations

/**
 * Preview function to display the BottomNavigationBar in the design preview.
 */
@Preview(showBackground = true)
@Composable
fun BottomNavPreview() {
    HabitflowAppTheme {
        BottomNavigationBar(
            currentRoute = NavDestinations.HOME,
            onItemClick = {}
        )
    }
}

/**
 * Represents a navigation item for the bottom navigation bar.
 *
 * @property route The navigation route associated with the item.
 * @property icon The drawable resource ID for the item's icon.
 * @property label The label text displayed under the icon.
 */
sealed class BottomNavItem(
    val route: String,
    val icon: Int,
    val label: String
) {
    object Home : BottomNavItem(
        route = NavDestinations.HOME,
        icon = R.drawable.ic_home,
        label = "Inicio"
    )

    object Habits : BottomNavItem(
        route = NavDestinations.HABITS,
        icon = R.drawable.ic_habits,
        label = "Hábitos"
    )

    object Stats : BottomNavItem(
        route = NavDestinations.HABITS,
        icon = R.drawable.ic_stats,
        label = "Estadísticas"
    )

    object Social : BottomNavItem(
        route = NavDestinations.HABITS,
        icon = R.drawable.ic_social,
        label = "Social"
    )
}

/**
 * A bottom navigation bar component for navigating between main sections of the app.
 *
 * @param currentRoute The currently selected navigation route.
 * @param onItemClick Callback invoked when a navigation item is clicked.
 * @param modifier Optional [Modifier] for customizing the layout.
 */
@Composable
fun BottomNavigationBar(
    currentRoute: String,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.Habits,
        BottomNavItem.Stats,
        BottomNavItem.Social
    )

    NavigationBar(
        modifier = modifier.height(64.dp),
        containerColor = Color.White,
        tonalElevation = 8.dp
    ) {
        items.forEach { item ->
            val selected = currentRoute == item.route

            NavigationBarItem(
                selected = selected,
                onClick = { onItemClick(item.route) },
                icon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = item.icon),
                        contentDescription = item.label,
                        modifier = Modifier.size(24.dp)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = AppTypography.labelMedium
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Black,
                    selectedTextColor = Color.Black,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    unselectedTextColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                    indicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                ),
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}