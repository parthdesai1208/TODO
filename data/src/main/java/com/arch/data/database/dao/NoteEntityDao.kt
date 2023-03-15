package com.arch.data.database.dao

import androidx.room.Dao
import com.arch.data.database.dao.base.BaseDao
import com.arch.data.entity.local.NoteEntity

@Dao
abstract class NoteEntityDao : BaseDao<NoteEntity>,NoteEntityUseCaseDao