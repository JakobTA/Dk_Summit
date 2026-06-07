package com.example.mk8dxplaylist.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.mk8dxplaylist.ui.screens.HomeScreen
import com.example.mk8dxplaylist.ui.screens.PlaylistEditorScreen
import com.example.mk8dxplaylist.ui.screens.RandomizerScreen
import com.example.mk8dxplaylist.ui.theme.AppTheme
import com.example.mk8dxplaylist.viewmodel.AppViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: AppViewModel = viewModel()
                    val navController = rememberNavController()

                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") {
                            HomeScreen(
                                viewModel = viewModel,
                                onEditPlaylist = { id -> navController.navigate("editor/$id") },
                                onNewPlaylist = {
                                    val id = viewModel.createPlaylist("New Playlist")
                                    navController.navigate("editor/$id")
                                },
                                onStartRandomizer = { id ->
                                    navController.navigate("randomizer/$id")
                                }
                            )
                        }

                        composable(
                            "editor/{playlistId}",
                            arguments = listOf(navArgument("playlistId") { type = NavType.StringType })
                        ) { back ->
                            val id = back.arguments?.getString("playlistId") ?: return@composable
                            PlaylistEditorScreen(
                                playlistId = id,
                                viewModel = viewModel,
                                onBack = { navController.popBackStack() }
                            )
                        }

                        composable(
                            "randomizer/{playlistId}",
                            arguments = listOf(navArgument("playlistId") { type = NavType.StringType })
                        ) {
                            RandomizerScreen(
                                viewModel = viewModel,
                                onBack = {
                                    viewModel.clearRandomizer()
                                    navController.popBackStack()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
