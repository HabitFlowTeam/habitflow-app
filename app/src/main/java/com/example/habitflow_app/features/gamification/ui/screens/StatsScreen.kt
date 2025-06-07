package com.example.habitflow_app.features.gamification.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.habitflow_app.core.ui.theme.Background
import com.example.habitflow_app.features.gamification.ui.components.SegmentedControl
import com.example.habitflow_app.features.gamification.ui.viewmodel.StatsGlobalViewModel
import com.example.habitflow_app.features.gamification.ui.viewmodel.StatsPersonalViewModel

/**
 * Main statistics screen that displays either personal or global statistics
 * using a segmented control for navigation between the two views.
 *
 * @param globalViewModel ViewModel for global statistics data (injected automatically)
 */
@Composable
fun StatsScreen(
    globalViewModel: StatsGlobalViewModel = hiltViewModel(),
    personalViewModel: StatsPersonalViewModel = hiltViewModel()
) {
    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp)
    ) {
        // Control segmentado para alternar entre vistas personales y globales
        SegmentedControl(
            options = listOf("Personales", "Globales"),
            selectedIndex = selectedTabIndex,
            onTabSelected = { selectedTabIndex = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Mostrar la pantalla correspondiente según la pestaña seleccionada
        when (selectedTabIndex) {
            0 -> StatsPersonalScreen(personalViewModel)
            1 -> StatsGlobalScreen(globalViewModel)
        }
    }
}
