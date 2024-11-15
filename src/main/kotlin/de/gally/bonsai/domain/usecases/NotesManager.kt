package de.gally.bonsai.domain.usecases

import de.gally.bonsai.domain.Note
import java.util.UUID

interface NotesManager {

    fun addNote(note: Note, bonsaiUUID: UUID): Note

    fun getAllNotesByBonsai(bonsaiUUID: UUID, page: Int, pageSize: Int): List<Note>

    fun editNote(note: Note, bonsaiUUID: UUID): Note

    fun deleteNote(noteUuid: UUID)
}