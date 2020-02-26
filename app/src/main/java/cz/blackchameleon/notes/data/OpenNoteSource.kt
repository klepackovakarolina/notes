package cz.blackchameleon.notes.data

import cz.blackchameleon.notes.framework.Note
import cz.blackchameleon.notes.framework.Result

interface OpenNoteSource {
    suspend fun getOpenNote(): Result<Note?>
    suspend fun setOpenNote(note: Note?)
}