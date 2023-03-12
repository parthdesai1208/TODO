package com.arch.data.source.note.local

import com.arch.data.database.dao.NoteEntityDao
import com.arch.data.entity.local.NoteEntity
import javax.inject.Inject

class NoteLocalDataSourceImpl @Inject constructor(private val noteEntityDao: NoteEntityDao) :
    NoteLocalDataSource {
    override suspend fun saveNote(noteEntity: NoteEntity): Boolean {
        return noteEntityDao.insert(noteEntity) > 0
    }
}