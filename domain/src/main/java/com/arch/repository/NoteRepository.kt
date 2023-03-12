package com.arch.repository

import com.arch.error.BaseError
import com.arch.utils.Either

interface NoteRepository {
    suspend fun insertNote(noteContent : String) : Either<BaseError,Boolean>
}