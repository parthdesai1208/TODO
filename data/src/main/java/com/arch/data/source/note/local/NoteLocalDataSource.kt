package com.arch.data.source.note.local

import com.arch.data.entity.local.NoteEntity

interface NoteLocalDataSource {
    suspend fun saveNote(noteEntity: NoteEntity): Boolean
}