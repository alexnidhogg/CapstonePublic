package sheridan.teodored.myapplication.forum

data class ForumThreadListElement (val Title : String, val Id : String, val callback: (Thread : String) -> Void?)