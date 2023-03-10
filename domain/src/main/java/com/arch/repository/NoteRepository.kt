package com.arch.repository


import androidx.paging.PagingConfig
import com.arch.entity.Note
import com.arch.error.DatabaseError
import com.arch.utils.Either

interface NoteRepository {
    suspend fun insertNote(noteId: Int, noteContent: String): Either<DatabaseError, Boolean>

    suspend fun getNotes(pagingConfig: PagingConfig): Either<DatabaseError, List<Note>>

    suspend fun deleteNote(noteId: String) : Either<DatabaseError,Boolean>
}