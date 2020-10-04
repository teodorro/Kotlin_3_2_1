data class Note (
    val id: Int,
    val title: String,
    val text: String,
    val comments: MutableList<Comment>
)
