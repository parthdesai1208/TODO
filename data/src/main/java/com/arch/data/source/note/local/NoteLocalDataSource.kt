package com.arch.data.source.note.local

import androidx.paging.PagingConfig
import com.arch.data.entity.local.NoteEntity
import com.arch.entity.Note

interface NoteLocalDataSource {
    suspend fun saveNote(noteEntity: NoteEntity): Boolean

     fun getNotes(pageConfig: PagingConfig) : List<Note>
}