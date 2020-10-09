import Exceptions.NoteNotFoundException
import org.junit.Assert.*
import org.junit.Test

class NoteServiceTest {

    // region Notes

    @Test
    fun add_Added() {
        val id = NoteService().add("title", "text")

        assertTrue(id > 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun add_EmptyTextAndTitle() {
        NoteService().add("", "")
    }

    @Test
    fun delete_ExistingNote() {
        val noteService = NoteService()
        val id = noteService.add("title", "text")

        val res = noteService.delete(id)

        assertTrue(res)
    }

    @Test
    fun delete_NotExistingNote() {
        val noteService = NoteService()
        noteService.add("title", "text")

        val res = noteService.delete(123)

        assertFalse(res)
    }

    @Test
    fun edit_ExistingNote() {
        val noteService = NoteService()
        val id = noteService.add("title", "text")

        val res = noteService.edit(id, "asd", "qwe")

        assertTrue(res)

        val note = noteService.getById(id)
        assertEquals(note.text, "qwe")
        assertEquals(note.title, "asd")
    }

    @Test
    fun edit_NotExistingNote() {
        val noteService = NoteService()
        noteService.add("title", "text")

        val res = noteService.edit(123, "asd", "qwe")

        assertFalse(res)
    }

    @Test
    fun edit_EmptyTextAndTitle() {
        val noteService = NoteService()
        val id = noteService.add("title", "text")

        val res = noteService.edit(id, "", "")

        assertFalse(res)
    }

    @Test
    fun get_ExistAll() {
        val noteService = NoteService()
        val id1 = noteService.add("title1", "text1")
        val id2 = noteService.add("title2", "text2")

        val notes = noteService.get(mutableListOf(id1, id2))

        assertEquals(2, notes.count())
    }

    @Test(expected = NoteNotFoundException::class)
    fun get_ExistNotAll() {
        val noteService = NoteService()
        val id1 = noteService.add("title1", "text1")
        noteService.add("title2", "text2")

        noteService.get(mutableListOf(id1, 123))
    }

    @Test(expected = NoteNotFoundException::class)
    fun get_NoNotesExist() {
        NoteService().get(mutableListOf(123))
    }

    @Test
    fun getById_NoteWithIdExists() {
        val noteService = NoteService()
        noteService.add("title1", "text1")
        val id2 = noteService.add("title2", "text2")

        val note = noteService.getById(id2)

        assertEquals(id2, note.id)
    }

    @Test(expected = NoteNotFoundException::class)
    fun getById_NoteWithIdNotExists() {
        val noteService = NoteService()
        noteService.add("title1", "text1")
        noteService.add("title2", "text2")

        noteService.getById(123)
    }

    @Test(expected = NoteNotFoundException::class)
    fun getById_NoNotesExist() {
        NoteService().getById(123)
    }

    // endregion Notes


    // region Comments

    @Test
    fun createComment_Added() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")

        val commentId = noteService.createComment(noteId, "asd")

        assertTrue(commentId > 0)
    }

    @Test(expected = NoteNotFoundException::class)
    fun createComment_NoteNotExists() {
        val noteService = NoteService()
        noteService.add("title", "text")

        noteService.createComment(123, "asd")
    }

    @Test(expected = IllegalArgumentException::class)
    fun createComment_CommentEmpty() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")

        noteService.createComment(noteId, "")
    }

    @Test
    fun deleteComment_Deleted() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")
        val commentId = noteService.createComment(noteId, "asd")

        val res = noteService.deleteComment(commentId)

        assertTrue(res)
    }

    @Test
    fun deleteComment_DeleteDeleted() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")
        val commentId = noteService.createComment(noteId, "asd")
        noteService.deleteComment(commentId)

        val res = noteService.deleteComment(commentId)

        assertFalse(res)
    }

    @Test
    fun deleteComment_CommentNotExists() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")
        noteService.createComment(noteId, "asd")

        val res = noteService.deleteComment(123)

        assertFalse(res)
    }

    @Test
    fun editComment_Edited() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")
        val commentId = noteService.createComment(noteId, "asd")

        val res = noteService.editComment(commentId, "qwe")

        assertTrue(res)
    }

    @Test
    fun editComment_CommentNotExists() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")
        noteService.createComment(noteId, "asd")

        val res = noteService.editComment(123, "qwe")
        assertFalse(res)
    }

    @Test
    fun editComment_CommentDeleted() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")
        val commentId = noteService.createComment(noteId, "asd")
        noteService.deleteComment(commentId)

        val res = noteService.editComment(123, "qwe")

        assertFalse(res)
    }

    @Test
    fun editComment_CommentEmpty() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")
        val commentId = noteService.createComment(noteId, "asd")

        val res = noteService.editComment(commentId, "")

        assertFalse(res)
    }

    @Test
    fun getComments_Got() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")
        noteService.createComment(noteId, "asd")
        noteService.createComment(noteId, "qwe")

        val comments = noteService.getComments(noteId)

        assertEquals(2, comments.count())
    }

    @Test(expected = NoteNotFoundException::class)
    fun getComments_NoteNotExists() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")
        noteService.createComment(noteId, "asd")
        noteService.createComment(noteId, "qwe")

        noteService.getComments(123)
    }

    @Test
    fun getComments_ReturnsWithoutDeleted() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")
        val commentId1 = noteService.createComment(noteId, "asd")
        noteService.createComment(noteId, "qwe")
        noteService.deleteComment(commentId1)

        val comments = noteService.getComments(noteId)

        assertEquals(1, comments.count())
    }

    @Test
    fun restoreComment_Restored() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")
        val commentId1 = noteService.createComment(noteId, "asd")
        noteService.deleteComment(commentId1)

        val res = noteService.restoreComment(commentId1)

        assertTrue(res)
    }

    @Test
    fun restoreComment_CommentNotExists() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")
        val commentId1 = noteService.createComment(noteId, "asd")
        noteService.deleteComment(commentId1)

        val res = noteService.restoreComment(123)

        assertFalse(res)
    }

    @Test
    fun restoreComment_RestoreExisting() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")
        val commentId1 = noteService.createComment(noteId, "asd")

        val res = noteService.restoreComment(commentId1)

        assertTrue(res)
    }

    @Test
    fun restoreComment_RestoreFromDeletedNote() {
        val noteService = NoteService()
        val noteId = noteService.add("title", "text")
        val commentId1 = noteService.createComment(noteId, "asd")
        noteService.delete(noteId)

        val res = noteService.restoreComment(commentId1)

        assertFalse(res)
    }

    // endregion Comments

}