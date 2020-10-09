fun main(){
    val noteService = NoteService();
    val noteId1 = noteService.add("title1", "text1")
    val noteId2 = noteService.add("title2", "text2")
    val commentId1 = noteService.createComment(noteId1, "comment1")
    val commentId2 = noteService.createComment(noteId1, "comment2")
    val commentId3 = noteService.createComment(noteId2, "comment3")
    val commentId4 = noteService.createComment(noteId2, "comment4")
    noteService.deleteComment(commentId1);

    var notes = noteService.get(mutableListOf(noteId1, noteId2));
    for (note in notes){
        println("Note with title '${note.title}'' and text '${note.text}'")
        var comments = noteService.getComments(note.id)
        for (comment in comments){
            println("Comment with message '${comment.message}'")
        }
    }
}