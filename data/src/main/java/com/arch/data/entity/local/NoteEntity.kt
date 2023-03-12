package com.arch.data.entity.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var noteId: Int = 0,
    val noteContent: String,
    val createdAt: Long? = System.currentTimeMillis()
)