package com.arch.data.source.note.local

import androidx.paging.PagingConfig
import com.arch.data.database.dao.NoteEntityDao
import com.arch.data.entity.local.NoteEntity
import com.arch.entity.Note
import javax.inject.Inject

class NoteLocalDataSourceImpl @Inject constructor(private val noteEntityDao: NoteEntityDao) :
    NoteLocalDataSource {
    override suspend fun saveNote(noteEntity: NoteEntity): Boolean {
        return noteEntityDao.insert(noteEntity) > 0
    }

    override fun getNotes(
        pageConfig: PagingConfig
    ): List<Note> {
        return noteEntityDao.getNote().map {
            it.transform()
        }
        /* return Pager(
             config = pageConfig,
             initialKey = 0,
             pagingSourceFactory = {
                 NoteLocalDataPagingSource(noteEntityDao = noteEntityDao)
             }
         )*/
    }

    override suspend fun deleteNote(noteId: Int): Boolean {
        return noteEntityDao.deleteByNoteId(noteId) > 0
    }
}