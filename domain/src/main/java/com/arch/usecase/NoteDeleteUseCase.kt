package com.arch.usecase

import com.arch.error.AppError
import com.arch.error.AppErrorType
import com.arch.error.BaseError
import com.arch.repository.NoteRepository
import com.arch.usecase.base.BaseUseCase
import com.arch.usecase.base.Params
import com.arch.utils.Either
import com.arch.utils.Validator
import javax.inject.Inject

class NoteDeleteUseCase @Inject constructor(private val noteRepository: NoteRepository) :
    BaseUseCase<NoteDeleteUseCase.AuthNoteId, Either<BaseError, Boolean>> {

    class AuthNoteId(val noteId: String) : Params {
        override fun verify(): Boolean {
            return if (Validator.isBlank(noteId)) throw AppError(
                appErrorType = AppErrorType.NoteIdEmpty,
            ) else {
                true
            }
        }

    }

    override suspend fun execute(params: AuthNoteId): Either<BaseError, Boolean> {
        return noteRepository.deleteNote(params.noteId)
    }
}