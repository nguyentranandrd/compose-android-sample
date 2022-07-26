package com.capt.cleanarchitecturenoteapp.feature_note.presentation.util

sealed class Screen(val route: String) {
    object SplashScreen : Screen("splash_screen")
    object NotesScreen : Screen("notes_screen")
    object AddEditNoteScreen : Screen("add_edit_note_screen")
}
