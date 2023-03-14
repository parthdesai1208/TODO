package com.arch.data.database.dao.base

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.arch.data.entity.local.NoteEntity

@Dao
interface BaseDao<T> {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(obj: List<T>): List<Long>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(obj: T?): Long

    @Query("select * from note order by createdAt DESC") //LIMIT :limit OFFSET :offset
    fun getNote(): List<NoteEntity> //limit: Int, offset: Int

    @Query("delete from note where noteId = :noteId")
    fun deleteByNoteId(noteId: Int) : Int
}
