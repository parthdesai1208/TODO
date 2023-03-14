package com.arch.presentation.model

import android.os.Parcelable
import com.arch.entity.Note
import com.arch.utils.mapper.BaseLayerDataTransformer
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotePresentation(
    var noteId: Int = 0,
    val noteContent: String = "",
    val createdAt: Long? = System.currentTimeMillis()
) : BaseLayerDataTransformer<NotePresentation, Note>(), Parcelable {

    override fun restore(data: Note): NotePresentation {
        return NotePresentation(
            noteId = data.noteId,
            noteContent = data.noteContent,
            createdAt = data.createdAt
        )
    }
}