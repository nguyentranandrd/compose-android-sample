package com.capt.cleanarchitecturenoteapp.feature_note.presentation.add_notes

import androidx.compose.ui.graphics.toArgb
import com.capt.cleanarchitecturenoteapp.feature_note.domain.model.Note

data class NoteUiState(
    val title: NoteTextFiledUiState = NoteTextFiledUiState(
        hint = "Enter title..."
    ),
    val content: NoteTextFiledUiState = NoteTextFiledUiState(
        hint = "Enter some content"
    ),
    val color: Int = Note.noteColors.random().toArgb()
)