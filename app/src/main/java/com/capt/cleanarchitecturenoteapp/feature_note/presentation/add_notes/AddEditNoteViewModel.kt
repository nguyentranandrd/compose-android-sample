package com.capt.cleanarchitecturenoteapp.feature_note.presentation.add_notes

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusState
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.capt.cleanarchitecturenoteapp.base.BaseViewModel
import com.capt.cleanarchitecturenoteapp.feature_note.domain.model.InvalidNoteException
import com.capt.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.capt.cleanarchitecturenoteapp.feature_note.domain.use_case.GetNoteByIdUseCase
import com.capt.cleanarchitecturenoteapp.feature_note.domain.use_case.InsertNoteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddEditNoteViewModel
@Inject
constructor(
    private val getNoteByIdUseCase: GetNoteByIdUseCase,
    private val insertNoteUseCase: InsertNoteUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<AddEditNoteViewModel.UIEvent, AddEditNoteViewModel.UIAction>() {

    private var _noteUiState by mutableStateOf(NoteUiState())
    val noteUiState: NoteUiState
        get() = _noteUiState

    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let { noteId ->
            if (noteId != -1)
                viewModelScope.launch {
                    getNoteByIdUseCase(noteId)?.also {
                        currentNoteId = it.id
                        _noteUiState = _noteUiState.copy(
                            title = _noteUiState.title.copy(
                                text = it.title,
                                isHintVisible = false
                            ),
                            content = _noteUiState.content.copy(
                                text = it.content,
                                isHintVisible = false
                            ),
                            color = it.color
                        )
                    }
                }
        }
    }

    override fun onEvent(event: UIEvent) {
        when (event) {
            is UIEvent.EnteredTitle -> {
                _noteUiState = noteUiState.copy(
                    title = noteUiState.title.copy(
                        text = event.value
                    )
                )
            }
            is UIEvent.ChangeTitleFocus -> {
                _noteUiState = noteUiState.copy(
                    title = noteUiState.title.copy(
                        isHintVisible = !event.focusState.isFocused && _noteUiState.title.text.isBlank()
                    )
                )
            }
            is UIEvent.EnteredContent -> {
                _noteUiState = noteUiState.copy(
                    content = noteUiState.content.copy(
                        text = event.value
                    )
                )
            }
            is UIEvent.ChangeContentFocus -> {
                _noteUiState = noteUiState.copy(
                    content = noteUiState.content.copy(
                        isHintVisible = !event.focusState.isFocused && noteUiState.content.text.isBlank()
                    )
                )
            }
            is UIEvent.ChangeColor ->
                _noteUiState = noteUiState.copy(
                    color = event.color
                )
            is UIEvent.SaveNote -> saveNote()

        }
    }

    private fun saveNote() {
        viewModelScope.launch {
            try {
                insertNoteUseCase(
                    Note(
                        title = noteUiState.title.text,
                        content = noteUiState.content.text,
                        timestamp = System.currentTimeMillis(),
                        color = noteUiState.color,
                        id = currentNoteId
                    )
                )
                emitAction(UIAction.BackToNotesScreen)
            } catch (e: InvalidNoteException) {
                emitAction(
                    UIAction.ShowSnackBar(
                        message = e.message ?: "Couldn't save note"
                    )
                )
            }
        }
    }

    sealed class UIAction {
        data class ShowSnackBar(val message: String) : UIAction()
        object BackToNotesScreen : UIAction()
    }

    sealed class UIEvent {
        data class EnteredTitle(val value: String) : UIEvent()
        data class ChangeTitleFocus(val focusState: FocusState) : UIEvent()
        data class EnteredContent(val value: String) : UIEvent()
        data class ChangeContentFocus(val focusState: FocusState) : UIEvent()
        data class ChangeColor(val color: Int) : UIEvent()
        object SaveNote : UIEvent()
    }

}