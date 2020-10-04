fun main(){
    val noteId1 = NoteService.add("title1", "text1")
    val noteId2 = NoteService.add("title2", "text2")
    val commentId1 = NoteService.createComment(noteId1, "comment1")
    val commentId2 = NoteService.createComment(noteId1, "comment2")
    val commentId3 = NoteService.createComment(noteId2, "comment3")
    val commentId4 = NoteService.createComment(noteId2, "comment4")
    NoteService.deleteComment(commentId1);

    var notes = NoteService.get(mutableListOf(noteId1, noteId2));
    for (note in notes){
        println("Note with title '${note.title}'' and text '${note.text}'")
        var comments = NoteService.getComments(note.id)
        for (comment in comments){
            println("Comment with message '${comment.message}'")
        }
    }
}