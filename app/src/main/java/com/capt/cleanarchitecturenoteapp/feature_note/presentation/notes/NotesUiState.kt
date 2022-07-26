package com.capt.cleanarchitecturenoteapp.feature_note.presentation.notes

import com.capt.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.capt.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.capt.cleanarchitecturenoteapp.feature_note.domain.util.OrderType

data class NotesUiState(
    val notes: List<Note> = emptyList(),
    val noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending),
    val isOrderSectionVisible: Boolean = false
)
