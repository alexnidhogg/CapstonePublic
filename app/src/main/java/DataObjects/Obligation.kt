package DataObjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Obligation (
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "name") val name: String?,
    @ColumnInfo(name = "description") val description: String?,
    @ColumnInfo(name = "due_year") val dueYear: Int?,
    @ColumnInfo(name = "due_month") val dueMonth: Int?,
    @ColumnInfo(name = "due_day") val dueDay: Int?,
    @ColumnInfo(name = "done") val done: Boolean?,
    @ColumnInfo(name = "time_estimate") val timeEstimate: Int?,
    @ColumnInfo(name = "repeat") val repeat: String?,
    @ColumnInfo(name = "days") val days: Int?,
    @ColumnInfo(name = "movable") val movable: Boolean?
)