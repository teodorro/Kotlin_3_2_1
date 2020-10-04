import Exceptions.NoteNotFoundException
import java.lang.IllegalArgumentException

object NoteService {
    private var nextId = 1;
    private var notes = mutableListOf<Note>();

    fun add(title: String, text: String): Int {
        if (title.isBlank() || text.isBlank())
            throw IllegalArgumentException("Text arguments should not be blank")
        var note = Note(nextId++, title, text, mutableListOf())
        notes.add(note)
        return note.id
    }

    fun createComment(note_id: Int, message: String): Int {
        if (message.isBlank())
            throw IllegalArgumentException("Message should not be blank")
        val note = getById(note_id)
        val comment = Comment(nextId++, message)
        note.comments.add(comment)
        return comment.id;
    }

    fun delete(note_id: Int): Boolean {
        var noteToDeleteInd: Int = -1
        for ((index, note) in notes.withIndex()) {
            if (note.id == note_id)
                noteToDeleteInd = index;
        }
        if (noteToDeleteInd >= 0) {
            notes.removeAt(noteToDeleteInd)
            return true;
        }
//        for (note in notes){
//            if (note.id == note_id && !note.deleted){
//                note.deleted = true;
//                return true;
//            }
//        }
        return false;
    }

    fun deleteComment(comment_id: Int): Boolean {
        var comment: Comment? = getComment(comment_id) ?: return false;
        if ((comment as Comment).deleted)
            return false;
        comment.deleted = true;
        return true;
    }

    fun edit(note_id: Int, title: String, text: String): Boolean {
        return try {
            if (title.isBlank() || text.isBlank())
                throw IllegalArgumentException("Text arguments should not be blank")
            var note = getById(note_id)
            var n = note.copy(title = title, text = text)
            notes.remove(note)
            notes.add(n)
            true
        } catch (e: IllegalArgumentException) {
            println(e.message)
            false
        } catch (e: NoteNotFoundException) {
            println(e.message)
            false
        }
    }

    fun editComment(comment_id: Int, message: String): Boolean {
        return try {
            if (message.isBlank())
                throw IllegalArgumentException("message should not be blank")
            var comment: Comment? = getComment(comment_id) ?: return false;
            (comment as Comment).message = message;
            true;
        } catch (e: IllegalArgumentException) {
            println(e.message)
            false
        }
    }

    fun get(note_ids: MutableList<Int>): List<Note> {
        var notesToReturn = mutableListOf<Note>()
        var noteIdToRemove: Int

        for (note in notes) {
            noteIdToRemove = -1
            for (noteId in note_ids) {
                if (note.id == noteId) {
                    notesToReturn.add(note)
                    noteIdToRemove = noteId
                }
            }
            if (noteIdToRemove > 0)
                note_ids.remove(noteIdToRemove)
        }

        if (note_ids.any())
            throw NoteNotFoundException("Note with note_id=${note_ids[0]} was not found")

        return notesToReturn
    }

    fun getById(note_id: Int): Note {
        for (note in notes) {
            if (note.id == note_id)
                return note
        }
        throw NoteNotFoundException("No note with note_id=$note_id")
    }

    fun getComments(note_id: Int): List<Comment> {
        var note = getById(note_id)
        var comments = mutableListOf<Comment>()
        for (comment in note.comments) {
            if (!comment.deleted)
                comments.add(comment)
        }
        return comments
    }

    fun restoreComment(comment_id: Int): Boolean {
        var comment: Comment? = getComment(comment_id) ?: return false
        (comment as Comment).deleted = false
        return true
    }

    private fun getComment(commentId: Int): Comment? {
        for (note in notes)
            for (comment in note.comments) {
                if (comment.id == commentId)
                    return comment
            }
        return null
    }

}