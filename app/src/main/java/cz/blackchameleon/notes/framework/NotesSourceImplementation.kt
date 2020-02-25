package cz.blackchameleon.notes.framework

import cz.blackchameleon.notes.data.NotesSource
import cz.blackchameleon.notes.domain.Note

class NotesSourceImplementation(
    private val notesRequestService: NotesRequestService
) : NotesSource {
    override suspend fun getNotes(): List<Note> = notesRequestService.getNotes()

    override suspend fun getNote(id: Int): Note = notesRequestService.getNote(id)

    override suspend fun editNote(id: Int, note: Note): Note = notesRequestService.editNote(id, note)

    override suspend fun createNote(note: Note): Note = notesRequestService.createNote(note)

    override suspend fun deleteNote(id: Int) = notesRequestService.deleteNote(id)
}