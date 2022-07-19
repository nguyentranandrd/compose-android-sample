package com.capt.cleanarchitecturenoteapp.feature_note.presentation.add_notes

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusState
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.capt.cleanarchitecturenoteapp.core.presentation.BaseViewModel
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
) : BaseViewModel<AddEditNoteViewModel.UIEvent, AddEditNoteViewModel.UIState>() {

    private val _noteTitle = mutableStateOf(
        NoteTextFiledState(
            hint = "Enter title..."
        )
    )
    val noteTitle: State<NoteTextFiledState> = _noteTitle

    private val _noteContent = mutableStateOf(
        NoteTextFiledState(
            hint = "Enter some content"
        )
    )
    val noteContent: State<NoteTextFiledState> = _noteContent

    private val _noteColor = mutableStateOf(Note.noteColors.random().toArgb())
    val noteColor: State<Int> = _noteColor


    private var currentNoteId: Int? = null

    init {
        savedStateHandle.get<Int>("noteId")?.let {
            if (it != -1)
                viewModelScope.launch {
                    getNoteByIdUseCase(it)?.also {
                        currentNoteId = it.id
                        _noteTitle.value = noteTitle.value.copy(
                            text = it.title,
                            isHintVisible = false
                        )

                        _noteContent.value = noteContent.value.copy(
                            text = it.content,
                            isHintVisible = false
                        )
                        _noteColor.value = it.color

                    }
                }
        }
    }

    override fun onEvent(event: UIEvent) {
        when (event) {
            is UIEvent.EnteredTitle -> {
                _noteTitle.value = noteTitle.value.copy(
                    text = event.value
                )
            }
            is UIEvent.ChangeTitleFocus -> {
                _noteTitle.value = noteTitle.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteTitle.value.text.isBlank()
                )
            }
            is UIEvent.EnteredContent -> {
                _noteContent.value = noteContent.value.copy(
                    text = event.value
                )
            }
            is UIEvent.ChangeContentFocus -> {
                _noteContent.value = noteContent.value.copy(
                    isHintVisible = !event.focusState.isFocused && noteContent.value.text.isBlank()
                )
            }
            is UIEvent.ChangeColor -> {
                _noteColor.value = event.color
            }
            is UIEvent.SaveNote -> {
                viewModelScope.launch {
                    try {
                        insertNoteUseCase(
                            Note(
                                title = noteTitle.value.text,
                                content = noteContent.value.text,
                                timestamp = System.currentTimeMillis(),
                                color = noteColor.value,
                                id = currentNoteId
                            )
                        )
                        emitUIEvent(UIState.BackToNotesScreen)
                    } catch (e: InvalidNoteException) {
                        emitUIEvent(
                            UIState.ShowSnackBar(
                                message = e.message ?: "Couldn't save note"
                            )
                        )
                    }
                }
            }
        }
    }

    sealed class UIState {
        data class ShowSnackBar(val message: String) : UIState()
        object BackToNotesScreen : UIState()
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