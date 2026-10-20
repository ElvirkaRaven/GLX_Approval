package com.example.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.documents.presentation.DocumentsRoute
import com.example.documents.presentation.DocumentsScreen
import com.example.documents.presentation.DocumentDetailViewModel
import com.example.documents.presentation.DocumentDetailScreen
import com.example.settings.SettingsRoute
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.documents.presentation.DocumentsViewModel
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext



private object Routes {
    const val DOCUMENTS = "documents"
    const val SETTINGS = "settings"
    const val DOCUMENT_DETAILS = "document_details/{documentId}"

    fun documentDetails(documentId: String) =
        "document_details/$documentId"
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val showBottomBar = currentRoute == Routes.DOCUMENTS ||
            currentRoute == Routes.SETTINGS

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar {
                    NavigationBarItem(
                        selected = currentRoute == Routes.DOCUMENTS,
                        onClick = {
                            navController.navigate(Routes.DOCUMENTS) {
                                popUpTo(Routes.DOCUMENTS) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Description,
                                contentDescription = "Документы"
                            )
                        },
                        label = { Text("Документы") }
                    )

                    NavigationBarItem(
                        selected = currentRoute == Routes.SETTINGS,
                        onClick = {
                            navController.navigate(Routes.SETTINGS) {
                                launchSingleTop = true
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Настройки"
                            )
                        },
                        label = { Text("Настройки") }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.DOCUMENTS,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Routes.DOCUMENTS) {
                val viewModel: DocumentsViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()
                val context = LocalContext.current

                DocumentsScreen(
                    uiState = uiState,
                    onRefresh = viewModel::loadDocuments,
                    onDocumentClick = { document ->
                        navController.navigate("document/${document.id}")
                    }
                )
            }

            composable(Routes.SETTINGS) {
                SettingsRoute()
            }

            composable(
                route = "document/{documentId}",
                arguments = listOf(
                    navArgument("documentId") {
                        type = NavType.StringType
                    }
                )
            ) {
                val viewModel: DocumentDetailViewModel = hiltViewModel()
                val uiState by viewModel.uiState.collectAsState()
                val context = LocalContext.current

                DocumentDetailScreen(
                    uiState = uiState,
                    onBack = {
                        navController.popBackStack()
                    },
                    onApprove = {
                        Toast.makeText(
                            context,
                            "Отправлено решение \"Согласовать\"",
                            Toast.LENGTH_SHORT
                        ).show()
                    },
                    onReject = {
                        Toast.makeText(
                            context,
                            "Отправлено решение \"Отклонить\"",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                )
            }

            composable(
                route = Routes.DOCUMENT_DETAILS,
                arguments = listOf(
                    navArgument("documentId") {
                        type = NavType.StringType
                    }
                )
            ) { backStackEntry ->
                val documentId = backStackEntry.arguments
                    ?.getString("documentId")
                    .orEmpty()

                // Временная заглушка до подключения передачи
                // выбранного объекта Document на экран деталей.
                Text("Детали документа: $documentId")
            }
        }
    }
}