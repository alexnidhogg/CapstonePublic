package Database

import DataObjects.Assignment
import DataObjects.Class
import DataObjects.TimeChunk
import DataObjects.Obligation
import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Class::class, Assignment::class, Obligation::class, TimeChunk::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun classDao(): ClassDao
    abstract fun assignmentDao() : AssignmentDao
    abstract fun obligationDao() : ObligationDao
    abstract fun timechunkDao() : TimeChunkDao
}
