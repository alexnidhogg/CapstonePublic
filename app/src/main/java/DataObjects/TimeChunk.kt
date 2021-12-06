package DataObjects

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TimeChunk(
    @PrimaryKey(autoGenerate = true) val uid: Int,
    @ColumnInfo(name = "obligation") val obligationId: Int,
    @ColumnInfo(name = "start_day") val startDay: Int?,
    @ColumnInfo(name = "end_day") val endDay: Int?,
    @ColumnInfo(name = "start_month") val startMonth: Int?,
    @ColumnInfo(name = "end_month") val endMonth: Int?,
    @ColumnInfo(name = "start_year") val startYear: Int?,
    @ColumnInfo(name = "end_year") val endYear: Int?,
    @ColumnInfo(name = "start_time") val startTime: Int?,
    @ColumnInfo(name = "end_time") val endTime: Int?
)
