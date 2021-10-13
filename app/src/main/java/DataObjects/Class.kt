package DataObjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Class (
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "class_name") val className: String?
)