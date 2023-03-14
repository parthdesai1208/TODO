package com.arch.entity

data class Note(
    var noteId: Int = 0,
    val noteContent: String = "",
    val createdAt: Long = 0
)