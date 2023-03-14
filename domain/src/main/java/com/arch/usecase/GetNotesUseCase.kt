package com.arch.usecase


import androidx.paging.PagingConfig
import com.arch.entity.Note
import com.arch.error.DatabaseError
import com.arch.repository.NoteRepository
import com.arch.usecase.base.BaseUseCase
import com.arch.usecase.base.Params
import com.arch.utils.Either
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository
) : BaseUseCase<GetNotesUseCase.GetNotesParams, Either<DatabaseError, List<Note>>> {

    class GetNotesParams(
        val pagingConfig: PagingConfig
    ) : Params {
        override fun verify(): Boolean {
            return true
        }

    }

    override suspend fun execute(params: GetNotesParams): Either<DatabaseError, List<Note>> {
        return noteRepository.getNotes(params.pagingConfig)
    }
}