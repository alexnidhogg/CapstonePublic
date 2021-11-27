package sheridan.teodored.myapplication.chat

data class ChatMenuListElement (val Title : String, val Id : String, val callback: (Thread : String) -> Void?, val LastUpdate : Int)