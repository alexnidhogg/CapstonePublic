package DataObjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Assignment(
    @PrimaryKey val uid: Int,
    @ColumnInfo (name = "class_name") val className: String?,
    @ColumnInfo (name= "assignment_grade") val assignmentGrade: Int?,
    @ColumnInfo (name = "assignment_name") val assignmentName: String?
)