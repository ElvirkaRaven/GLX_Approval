package com.example.navigation

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable

@Composable
fun BottomNavigationBar(
    currentRoute: String?,
    onNavigate: (String) -> Unit
) {
    NavigationBar {
        NavigationBarItem(
            selected = currentRoute == AppDestination.DOCUMENTS,
            onClick = {
                onNavigate(AppDestination.DOCUMENTS)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = "Документы"
                )
            },
            label = {
                Text("Документы")
            }
        )

        NavigationBarItem(
            selected = currentRoute == AppDestination.SETTINGS,
            onClick = {
                onNavigate(AppDestination.SETTINGS)
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Настройки"
                )
            },
            label = {
                Text("Настройки")
            }
        )
    }
}