package com.capt.cleanarchitecturenoteapp.feature_note.presentation.notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.capt.cleanarchitecturenoteapp.base.BaseViewModel
import com.capt.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.capt.cleanarchitecturenoteapp.feature_note.domain.use_case.DeleteNoteUseCase
import com.capt.cleanarchitecturenoteapp.feature_note.domain.use_case.GetNotesUseCase
import com.capt.cleanarchitecturenoteapp.feature_note.domain.use_case.InsertNoteUseCase
import com.capt.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.capt.cleanarchitecturenoteapp.feature_note.domain.util.OrderType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NotesViewModel
@Inject
constructor(
    private val getNotesUseCase: GetNotesUseCase,
    private val deleteNoteUseCase: DeleteNoteUseCase,
    private val insertNoteUseCase: InsertNoteUseCase
) : BaseViewModel<NotesViewModel.UIEvent, NotesViewModel.UIAction>() {

    private var _noteState by mutableStateOf(NotesUiState())
    val noteState: NotesUiState
        get() = _noteState

    private var recentlyDeleteNote: Note? = null

    private var getNotesJob: Job? = null

    init {
        getNotes(NoteOrder.Date(OrderType.Descending))
    }

    override fun onEvent(event: UIEvent) {
        when (event) {
            is UIEvent.Order -> {
                if (noteState.noteOrder::class == event.noteOrder::class &&
                    noteState.noteOrder.orderType == event.noteOrder.orderType
                )
                    return
                getNotes(event.noteOrder)
            }
            is UIEvent.DeleteNote -> {
                viewModelScope.launch {
                    deleteNoteUseCase(event.note)
                    recentlyDeleteNote = event.note
                }
            }
            is UIEvent.RestoreNote -> {
                viewModelScope.launch {
                    insertNoteUseCase(recentlyDeleteNote ?: return@launch)
                    recentlyDeleteNote = null
                }
            }
            is UIEvent.ToggleOrderSection -> {
                _noteState = noteState.copy(
                    isOrderSectionVisible = !noteState.isOrderSectionVisible
                )
            }
        }
    }

    private fun getNotes(noteOrder: NoteOrder) {
        getNotesJob?.cancel()
        getNotesJob = getNotesUseCase(noteOrder)
            .onEach { notes ->
                _noteState = noteState.copy(
                    notes = notes,
                    noteOrder = noteOrder
                )
            }
            .launchIn(viewModelScope)
    }

    sealed class UIAction

    sealed class UIEvent {
        data class Order(val noteOrder: NoteOrder) : UIEvent()
        data class DeleteNote(val note: Note) : UIEvent()
        object RestoreNote : UIEvent()
        object ToggleOrderSection : UIEvent()
    }

}