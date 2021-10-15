package Database

import DataObjects.Assignment
import DataObjects.Class
import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import java.security.AccessControlContext


@Database(entities = [Class::class, Assignment::class], version = 4)
abstract class AppDatabase : RoomDatabase() {
    abstract fun classDao(): ClassDao
    abstract fun assignmentDao() : AssignmentDao

}
