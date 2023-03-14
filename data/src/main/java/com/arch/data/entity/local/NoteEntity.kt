package com.arch.data.entity.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.arch.entity.Note
import com.arch.utils.mapper.BaseLayerDataTransformer

@Entity(tableName = "note")
data class NoteEntity(
    @PrimaryKey(autoGenerate = true)
    var noteId: Int = 0,
    val noteContent: String,
    val createdAt: Long? = System.currentTimeMillis()
) : BaseLayerDataTransformer<NoteEntity,Note>(){
    override fun transform(): Note {
        return Note(
            noteId = noteId,
            noteContent = noteContent,
            createdAt = createdAt ?: System.currentTimeMillis()
        )
    }

    override fun transform(from: Collection<NoteEntity>): Collection<Note> {
        return super.transform(from)
    }
}