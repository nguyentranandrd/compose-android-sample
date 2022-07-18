package com.capt.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.capt.cleanarchitecturenoteapp.feature_note.domain.model.InvalidNoteException
import com.capt.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.capt.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class InsertNoteUseCase
@Inject
constructor(
    private val noteRepository: NoteRepository
) {
    @Throws(InvalidNoteException::class)
    suspend operator fun invoke(note: Note) {
        if (note.title.isBlank())
            throw InvalidNoteException("The title of the note can't be empty")
        if (note.content.isBlank())
            throw InvalidNoteException("The content of the note can't be empty")
        noteRepository.insertNote(note)
    }
}