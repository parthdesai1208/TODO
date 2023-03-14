package com.arch.presentation.viewmodels.note

import androidx.databinding.Observable
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import com.arch.entity.Note
import com.arch.error.DatabaseError
import com.arch.errors.android.handler.IAndroidExceptionHandler
import com.arch.logger.AppLogger
import com.arch.permissions.android.IAndroidPermissionsController
import com.arch.presentation.model.NotePresentation
import com.arch.presentation.viewmodels.base.ObservableBaseViewModel
import com.arch.usecase.GetNotesUseCase
import com.arch.utils.Either
import com.arch.utils.RequestManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteListingViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    private val getNotesUseCase: GetNotesUseCase,
    exceptionHandler: IAndroidExceptionHandler,
    permissionHandler: IAndroidPermissionsController,
    logger: AppLogger
) : ObservableBaseViewModel(exceptionHandler, permissionHandler, logger), Observable {
    private val _noteListingPagingFlow: MutableSharedFlow<List<NotePresentation>> =
        MutableSharedFlow()

    val noteListingPagingFlow: SharedFlow<List<NotePresentation>> = _noteListingPagingFlow

    private val _positionFlow: MutableStateFlow<Int> =
        MutableStateFlow(savedStateHandle[positionKey] ?: 0)

    val positionFlow: StateFlow<Int> = _positionFlow

    companion object {
        const val positionKey = "position"
    }

    fun getNote() {
        viewModelScope.launch {
            exceptionHandler.handle {
                val getNotesParams = GetNotesUseCase.GetNotesParams(
                    PagingConfig(pageSize = 10, prefetchDistance = 5, initialLoadSize = 10)
                )
                object : RequestManager<List<Note>>(params = getNotesParams) {
                    override suspend fun createCall(): Either<DatabaseError, List<Note>> {
                        return getNotesUseCase.execute(params = getNotesParams)
                    }
                }.asFlow().collect {
                    it.data?.let { it1 ->
                        _noteListingPagingFlow.emit(it1.map { it2 ->
                            NotePresentation().restore(it2)
                        })
                    }
                    /*it.data?.collect { it1 ->
                        _noteListingPagingFlow.emit(it1.map { it2 ->
                            NotePresentation().restore(it2)
                        })
                    }*/
                }
            }.catch<Exception> {
                false
            }.execute()
        }
    }

    fun setScrollPosition(position: Int) {
        _positionFlow.value = position
        savedStateHandle[positionKey] = position
    }
}