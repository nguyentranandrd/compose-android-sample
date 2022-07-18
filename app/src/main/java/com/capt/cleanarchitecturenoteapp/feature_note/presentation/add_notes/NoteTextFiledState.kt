package com.capt.cleanarchitecturenoteapp.feature_note.presentation.add_notes

data class NoteTextFiledState(
    val text: String = "",
    val hint: String = "",
    val isHintVisible: Boolean = true
)
