package com.capt.cleanarchitecturenoteapp.feature_note.presentation

import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.capt.cleanarchitecturenoteapp.base.BaseComposeActivity
import com.capt.cleanarchitecturenoteapp.feature_note.presentation.add_notes.AddEditNoteScreen
import com.capt.cleanarchitecturenoteapp.feature_note.presentation.notes.NotesScreen
import com.capt.cleanarchitecturenoteapp.feature_note.presentation.splash.SplashScreen
import com.capt.cleanarchitecturenoteapp.feature_note.presentation.util.Screen
import com.capt.cleanarchitecturenoteapp.ui.theme.CleanArchitectureNoteAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseComposeActivity() {
    @Composable
    override fun Content() {
        CleanArchitectureNoteAppTheme {
            Surface(color = MaterialTheme.colors.background) {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = Screen.SplashScreen.route
                ) {
                    composable(route = Screen.SplashScreen.route) {
                        SplashScreen(navController = navController)
                    }
                    composable(route = Screen.NotesScreen.route) {
                        NotesScreen(navController = navController)
                    }
                    composable(route = Screen.AddEditNoteScreen.route + "?noteId={noteId}&noteColor={noteColor}",
                        arguments = listOf(
                            navArgument(name = "noteId") {
                                type = NavType.IntType
                                defaultValue = -1
                            },
                            navArgument(name = "noteColor") {
                                type = NavType.IntType
                                defaultValue = -1
                            }
                        )) {
                        val color = it.arguments?.get("noteColor") as Int
                        AddEditNoteScreen(navController = navController, noteColor = color)
                    }

                }
            }
        }
    }
}

