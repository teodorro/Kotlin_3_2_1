import Exceptions.NoteNotFoundException
import org.junit.Assert.*
import org.junit.Test

class NoteServiceTest {

    // region Notes

    @Test
    fun add_Added() {
        val id = NoteService.add("title", "text")

        assertTrue(id > 0)
    }

    @Test(expected = IllegalArgumentException::class)
    fun add_EmptyTextAndTitle() {
        NoteService.add("", "")
    }

    @Test
    fun delete_ExistingNote() {
        val id = NoteService.add("title", "text")

        val res = NoteService.delete(id)

        assertTrue(res)
    }

    @Test()
    fun delete_NotExistingNote() {
        val id = NoteService.add("title", "text")

        val res = NoteService.delete(123)

        assertFalse(res)
    }

    @Test
    fun edit_ExistingNote() {
        val id = NoteService.add("title", "text")

        val res = NoteService.edit(id, "asd", "qwe")

        assertTrue(res)

        var note = NoteService.getById(id)
        assertEquals(note.text, "qwe")
        assertEquals(note.title, "asd")
    }

    @Test
    fun edit_NotExistingNote() {
        val id = NoteService.add("title", "text")

        val res = NoteService.edit(123, "asd", "qwe")

        assertFalse(res)
    }

    @Test()
    fun edit_EmptyTextAndTitle() {
        val id = NoteService.add("title", "text")

        val res = NoteService.edit(id, "", "")

        assertFalse(res);
    }

    @Test
    fun get_ExistAll() {
        val id1 = NoteService.add("title1", "text1")
        val id2 = NoteService.add("title2", "text2")

        val notes = NoteService.get(mutableListOf(id1, id2))

        assertEquals(2, notes.count())
    }

    @Test(expected = NoteNotFoundException::class)
    fun get_ExistNotAll() {
        val id1 = NoteService.add("title1", "text1")
        val id2 = NoteService.add("title2", "text2")

        val notes = NoteService.get(mutableListOf(id1, 123))
    }

    @Test(expected = NoteNotFoundException::class)
    fun get_NoNotesExist() {
        val notes = NoteService.get(mutableListOf(123))
    }

    @Test
    fun getById_NoteWithIdExists() {
        val id1 = NoteService.add("title1", "text1")
        val id2 = NoteService.add("title2", "text2")

        val note = NoteService.getById(id2)

        assertEquals(id2, note.id)
    }

    @Test(expected = NoteNotFoundException::class)
    fun getById_NoteWithIdNotExists() {
        val id1 = NoteService.add("title1", "text1")
        val id2 = NoteService.add("title2", "text2")

        val note = NoteService.getById(123)
    }

    @Test(expected = NoteNotFoundException::class)
    fun getById_NoNotesExist() {
        val note = NoteService.getById(123)
    }

    // endregion Notes


    // region Comments

    @Test
    fun createComment_Added() {
        val noteId = NoteService.add("title", "text")

        val commentId = NoteService.createComment(noteId, "asd")

        assertTrue(commentId > 0)
    }

    @Test(expected = NoteNotFoundException::class)
    fun createComment_NoteNotExists() {
        val noteId = NoteService.add("title", "text")

        val commentId = NoteService.createComment(123, "asd")
    }

    @Test(expected = IllegalArgumentException::class)
    fun createComment_CommentEmpty() {
        val noteId = NoteService.add("title", "text")

        val commentId = NoteService.createComment(noteId, "")
    }

    @Test
    fun deleteComment_Deleted() {
        val noteId = NoteService.add("title", "text")
        val commentId = NoteService.createComment(noteId, "asd")

        var res = NoteService.deleteComment(commentId);

        assertTrue(res)
    }

    @Test
    fun deleteComment_DeleteDeleted() {
        val noteId = NoteService.add("title", "text")
        val commentId = NoteService.createComment(noteId, "asd")
        var res0 = NoteService.deleteComment(commentId);

        var res = NoteService.deleteComment(commentId);

        assertFalse(res)
    }

    @Test
    fun deleteComment_CommentNotExists() {
        val noteId = NoteService.add("title", "text")
        val commentId = NoteService.createComment(noteId, "asd")

        var res = NoteService.deleteComment(123)

        assertFalse(res)
    }

    @Test
    fun editComment_Edited() {
        val noteId = NoteService.add("title", "text")
        val commentId = NoteService.createComment(noteId, "asd")

        var res = NoteService.editComment(commentId, "qwe")

        assertTrue(res)
    }

    @Test
    fun editComment_CommentNotExists() {
        val noteId = NoteService.add("title", "text")
        val commentId = NoteService.createComment(noteId, "asd")

        val res = NoteService.editComment(123, "qwe")
        assertFalse(res)
    }

    @Test
    fun editComment_CommentDeleted() {
        val noteId = NoteService.add("title", "text")
        val commentId = NoteService.createComment(noteId, "asd")
        NoteService.deleteComment(commentId)

        val res = NoteService.editComment(123, "qwe")

        assertFalse(res)
    }

    @Test
    fun editComment_CommentEmpty() {
        val noteId = NoteService.add("title", "text")
        val commentId = NoteService.createComment(noteId, "asd")

        val res = NoteService.editComment(commentId, "")

        assertFalse(res)
    }

    @Test
    fun getComments_Got() {
        val noteId = NoteService.add("title", "text")
        val commentId1 = NoteService.createComment(noteId, "asd")
        val commentId2 = NoteService.createComment(noteId, "qwe")

        var comments = NoteService.getComments(noteId)

        assertEquals(2, comments.count())
    }

    @Test(expected = NoteNotFoundException::class)
    fun getComments_NoteNotExists() {
        val noteId = NoteService.add("title", "text")
        val commentId1 = NoteService.createComment(noteId, "asd")
        val commentId2 = NoteService.createComment(noteId, "qwe")

        var comments = NoteService.getComments(123)
    }

    @Test
    fun getComments_ReturnsWithoutDeleted() {
        val noteId = NoteService.add("title", "text")
        val commentId1 = NoteService.createComment(noteId, "asd")
        val commentId2 = NoteService.createComment(noteId, "qwe")
        NoteService.deleteComment(commentId1)

        var comments = NoteService.getComments(noteId)

        assertEquals(1, comments.count())
    }

    @Test
    fun restoreComment_Restored() {
        val noteId = NoteService.add("title", "text")
        val commentId1 = NoteService.createComment(noteId, "asd")
        NoteService.deleteComment(commentId1)

        var res = NoteService.restoreComment(commentId1)

        assertTrue(res)
    }

    @Test
    fun restoreComment_CommentNotExists() {
        val noteId = NoteService.add("title", "text")
        val commentId1 = NoteService.createComment(noteId, "asd")
        NoteService.deleteComment(commentId1)

        var res = NoteService.restoreComment(123)

        assertFalse(res)
    }

    @Test
    fun restoreComment_RestoreExisting() {
        val noteId = NoteService.add("title", "text")
        val commentId1 = NoteService.createComment(noteId, "asd")

        var res = NoteService.restoreComment(commentId1)

        assertTrue(res)
    }

    @Test
    fun restoreComment_RestoreFromDeletedNote() {
        val noteId = NoteService.add("title", "text")
        val commentId1 = NoteService.createComment(noteId, "asd")
        NoteService.delete(noteId)

        var res = NoteService.restoreComment(commentId1)

        assertFalse(res)
    }

    // endregion Comments

}