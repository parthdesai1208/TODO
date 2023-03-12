package com.arch.data.repository

import com.arch.data.entity.local.NoteEntity
import com.arch.data.source.note.local.NoteLocalDataSource
import com.arch.error.BaseError
import com.arch.error.DatabaseError
import com.arch.repository.NoteRepository
import com.arch.utils.Either
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteLocalDataSource: NoteLocalDataSource
) : NoteRepository {
    override suspend fun insertNote(noteContent: String): Either<BaseError, Boolean> {
        val noteEntity = NoteEntity(noteContent = noteContent)
        val databaseStatus = noteLocalDataSource.saveNote(noteEntity)
        return if (databaseStatus) {
            Either.Right(true)
        } else {
            Either.Left(DatabaseError(
                message = "Oops, something went wrong! There was an error with the database",
                dbErrorCode = 1 //represent generic error in SQLite database
            ))
        }
    }
}