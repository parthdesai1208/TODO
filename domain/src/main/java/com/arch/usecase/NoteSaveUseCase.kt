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

class NoteSaveUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
) : BaseUseCase<NoteSaveUseCase.AuthNoteContent, Either<BaseError, Boolean>> {

    class AuthNoteContent(val noteContent: String) : Params {
        override fun verify(): Boolean {
            return if (Validator.isBlank(noteContent)) throw AppError(
                appErrorType = AppErrorType.NoteContentEmpty,
            ) else {
                true
            }
        }

    }

    override suspend fun execute(params: AuthNoteContent): Either<BaseError, Boolean> {
        return noteRepository.insertNote(params.noteContent)
    }
}