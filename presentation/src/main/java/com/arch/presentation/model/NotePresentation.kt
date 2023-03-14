package com.arch.presentation.model

import com.arch.entity.Note
import com.arch.utils.mapper.BaseLayerDataTransformer

data class NotePresentation(
    var noteId: Int = 0,
    val noteContent: String = "",
    val createdAt: Long? = System.currentTimeMillis()
) : BaseLayerDataTransformer<NotePresentation, Note>() {

    override fun restore(data: Note): NotePresentation {
        return NotePresentation(
            noteId = data.noteId,
            noteContent = data.noteContent,
            createdAt = data.createdAt
        )
    }
}