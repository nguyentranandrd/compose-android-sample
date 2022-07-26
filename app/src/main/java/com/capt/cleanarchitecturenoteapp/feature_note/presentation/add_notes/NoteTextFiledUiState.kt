package com.capt.cleanarchitecturenoteapp.feature_note.presentation.add_notes

data class NoteTextFiledUiState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)
