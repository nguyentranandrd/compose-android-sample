package com.capt.cleanarchitecturenoteapp.feature_note.domain.use_case

import com.capt.cleanarchitecturenoteapp.feature_note.domain.model.Note
import com.capt.cleanarchitecturenoteapp.feature_note.domain.repository.NoteRepository
import com.capt.cleanarchitecturenoteapp.feature_note.domain.util.NoteOrder
import com.capt.cleanarchitecturenoteapp.feature_note.domain.util.OrderType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetNotesUseCase
@Inject
constructor(
    private val noteRepository: NoteRepository
) {
    operator fun invoke(noteOrder: NoteOrder = NoteOrder.Date(OrderType.Descending)): Flow<List<Note>> {
        return noteRepository.getNotes().map {
            when (noteOrder.orderType) {
                is OrderType.Ascending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> it.sortedBy { it.title.lowercase() }
                        is NoteOrder.Date -> it.sortedBy { it.timestamp }
                        is NoteOrder.Color -> it.sortedBy { it.color }

                    }
                }
                is OrderType.Descending -> {
                    when (noteOrder) {
                        is NoteOrder.Title -> it.sortedByDescending { it.title.lowercase() }
                        is NoteOrder.Date -> it.sortedByDescending { it.timestamp }
                        is NoteOrder.Color -> it.sortedByDescending { it.color }
                    }
                }
            }
        }
    }


}