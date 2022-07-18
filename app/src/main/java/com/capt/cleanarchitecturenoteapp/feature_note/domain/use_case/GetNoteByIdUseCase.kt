package com.capt.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.capt.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.capt.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNoteByIdUseCase
@Inject
constructor(private val noteRepository: NoteRepository) {
    suspend operator fun invoke(id: Int): Note? = noteRepository.getNoteById(id)
}