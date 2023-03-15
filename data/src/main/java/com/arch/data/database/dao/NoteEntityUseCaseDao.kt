package com.arch.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.arch.data.entity.local.NoteEntity


@Dao
interface NoteEntityUseCaseDao {
    //this abstraction specific to NoteEntityDao
    @Query("select * from note order by createdAt DESC") //LIMIT :limit OFFSET :offset
    fun getNote(): List<NoteEntity> //limit: Int, offset: Int

    @Query("delete from note where noteId = :noteId")
    fun deleteByNoteId(noteId: Int): Int
}