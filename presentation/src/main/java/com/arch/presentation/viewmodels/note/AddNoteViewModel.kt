package com.arch.presentation.viewmodels.note

import androidx.lifecycle.viewModelScope
import com.arch.error.BaseError
import com.arch.errors.android.handler.IAndroidExceptionHandler
import com.arch.logger.AppLogger
import com.arch.permissions.android.IAndroidPermissionsController
import com.arch.presentation.di.qualifier.AddNoteViewModelExceptionHandler
import com.arch.presentation.viewmodels.base.BaseViewModel
import com.arch.usecase.NoteDeleteUseCase
import com.arch.usecase.NoteSaveUseCase
import com.arch.utils.Either
import com.arch.utils.RequestManager
import com.arch.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val noteSaveUseCase: NoteSaveUseCase,
    private val noteDeleteUseCase: NoteDeleteUseCase,
    @AddNoteViewModelExceptionHandler exceptionHandler: IAndroidExceptionHandler,
    permissionHandler: IAndroidPermissionsController,
    logger: AppLogger
) : BaseViewModel(exceptionHandler, permissionHandler, logger) {

    private val _noteSavingFlow: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(Resource.error(""))

    val noteSavingFlow: StateFlow<Resource<Boolean>> = _noteSavingFlow

    private val _noteDeleteFlow: MutableStateFlow<Resource<Boolean>> =
        MutableStateFlow(Resource.error(""))

    val noteDeleteFlow: StateFlow<Resource<Boolean>> = _noteDeleteFlow


    fun saveNote(noteId: Int, noteContent: String) {
        viewModelScope.launch {
            exceptionHandler.handle {
                val authParams = NoteSaveUseCase.AuthNoteContent(noteId, noteContent)
                object : RequestManager<Boolean>(params = authParams) {
                    override suspend fun createCall(): Either<BaseError, Boolean> {
                        return noteSaveUseCase.execute(authParams)
                    }
                }.asFlow().collect {
                    _noteSavingFlow.value = Resource(
                        status = it.status,
                        data = it.data ?: false,
                        message = it.message,
                        error = it.error
                    )
                }
            }.catch<Exception> {
                false
            }.execute()
        }
    }

    fun deleteNote(noteId: String) {
        viewModelScope.launch {
            exceptionHandler.handle {
                val authParams = NoteDeleteUseCase.AuthNoteId(noteId)
                object : RequestManager<Boolean>(params = authParams) {
                    override suspend fun createCall(): Either<BaseError, Boolean> {
                        return noteDeleteUseCase.execute(authParams)
                    }
                }.asFlow().collect {
                    _noteDeleteFlow.value = Resource(
                        status = it.status,
                        data = it.data ?: false,
                        message = it.message,
                        error = it.error
                    )
                }
            }.catch<Exception> {
                false
            }.execute()
        }
    }
}