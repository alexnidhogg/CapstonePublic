package sheridan.teodored.myapplication.forum

import android.view.View
import java.util.*

data class ForumPostListElement(val Author : String, val UID : String, val Message : String, val TimePosted : Date, val Id : String, val Press : (Id : Int, Anchor : View, User : String) -> Void?)
