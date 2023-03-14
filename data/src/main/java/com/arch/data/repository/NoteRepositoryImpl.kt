package com.arch.data.repository

import androidx.paging.PagingConfig
import com.arch.data.entity.local.NoteEntity
import com.arch.data.source.note.local.NoteLocalDataSource
import com.arch.entity.Note
import com.arch.error.DatabaseError
import com.arch.repository.NoteRepository
import com.arch.utils.Either
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteLocalDataSource: NoteLocalDataSource
) : NoteRepository {
    override suspend fun insertNote(noteContent: String): Either<DatabaseError, Boolean> {
        val noteEntity = NoteEntity(noteContent = noteContent)
        val databaseStatus = noteLocalDataSource.saveNote(noteEntity)
        return if (databaseStatus) {
            Either.Right(true)
        } else {
            Either.Left(
                DatabaseError(
                    message = "Oops, something went wrong! There was an error with the database",
                    dbErrorCode = 1 //represent generic error in SQLite database
                )
            )
        }
    }

    override suspend fun getNotes(pagingConfig: PagingConfig): Either<DatabaseError, List<Note>> {
       /* var returnResponse: Either<DatabaseError, Pager<Int, Note>> =
            Either.Left(DatabaseError(message = "", dbErrorCode = 1))*/
        val notes = noteLocalDataSource.getNotes(pagingConfig)
        return if (notes.isNotEmpty()) {
            Either.Right(notes)
        } else {
            Either.Left(
                DatabaseError(
                    message = "Oops, something went wrong! There was an error with the database",
                    dbErrorCode = 1 //represent generic error in SQLite database
                )
            )
        }
        /* val live = notes.liveData.value
         live?.map {
             returnResponse = if (it.noteContent.isNotBlank()) {
                 Either.Right(notes)
             } else {
                 Either.Left(
                     DatabaseError(
                         message = "Oops, something went wrong! There was an error with the database",
                         dbErrorCode = 1 //represent generic error in SQLite database
                     )
                 )
             }
         } ?: kotlin.run {
             returnResponse = Either.Left(
                 DatabaseError(
                     message = "Oops, something went wrong! There was an error with the database",
                     dbErrorCode = 1 //represent generic error in SQLite database
                 )
             )
         }*/
        //return returnResponse
    }
}