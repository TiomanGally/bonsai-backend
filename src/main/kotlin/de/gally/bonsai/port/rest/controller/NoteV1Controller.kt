package de.gally.bonsai.port.rest.controller

import de.gally.bonsai.domain.Note
import de.gally.bonsai.domain.usecases.NotesManager
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/v1/bonsais")
@Validated
class NoteV1Controller(
    private val notesManager: NotesManager
) {

    @GetMapping("/{bonsai-uuid}/notes")
    fun getAllNotesByBonsai(
        @PathVariable("bonsai-uuid") bonsaiUuid: UUID,
        @RequestParam("pageSize", defaultValue = "20") pageSize: Int,
        @RequestParam("page", defaultValue = "1") page: Int,
    ): List<Note> = notesManager.getAllNotesByBonsai(bonsaiUuid, page, pageSize)

    @PostMapping("/{bonsai-uuid}/notes")
    fun addNoteToBonsai(
        @PathVariable("bonsai-uuid") bonsaiUuid: UUID,
        @RequestBody note: Note
    ): Note = notesManager.addNote(note, bonsaiUuid)

    @PutMapping("/{bonsai-uuid}/notes")
    fun editNoteByBonsai(
        @PathVariable("bonsai-uuid") bonsaiUuid: UUID,
        @RequestBody note: Note,
    ): Note = notesManager.editNote(note, bonsaiUuid)

    @DeleteMapping("/notes/{uuid}")
    fun deleteNote(
        @PathVariable uuid: UUID
    ) = notesManager.deleteNote(uuid)
}
