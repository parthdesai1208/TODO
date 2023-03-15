package com.arch.template

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.arch.data.database.AppDatabase
import com.arch.data.database.dao.NoteEntityDao
import com.arch.data.entity.local.NoteEntity
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NoteDataBaseTest : TestCase() {

    private lateinit var db: AppDatabase

    private lateinit var dao: NoteEntityDao

    @Before
    public override fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context,
            AppDatabase::class.java
        ) //create temp. database in memory
            .build()
        dao = db.noteDao()

    }

    @After
    fun close() {
        db.close() //close db
    }

    @Test
    fun writeAndRead() = runBlocking {
        val note = NoteEntity(noteId = 1, noteContent = "I am testing Room db")
        dao.insert(note)
        val notes = dao.getNote()
        assertTrue(notes.contains(note))
    }

    @Test
    fun checkUpdate() = runBlocking {
        var note = NoteEntity(noteId = 1, noteContent = "1st time")
        val isInserted = dao.insert(note)
        assertEquals(isInserted, 1)  //check for 1st time insert to db

        note = NoteEntity(noteId = 1, noteContent = "2nd time")
        val isUpdateDone = dao.insert(note)
        assertEquals(isUpdateDone, 1) //check for update same note

        val notes = dao.getNote()
        assertTrue(notes.contains(note)) //check whether updated notes in list or not
    }

    @Test
    fun checkDelete() = runBlocking {
        val note = NoteEntity(noteId = 1, noteContent = "note is going to deleted")
        dao.insert(note)
        val deletedRow = dao.deleteByNoteId(1)
        assertEquals(deletedRow, 1)  //check affected rows
        val notes = dao.getNote()
        assertFalse(notes.contains(note)) //double check to make sure we don't have deleted notes
    }

}