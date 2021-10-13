package Database

import DataObjects.Class
import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import java.security.AccessControlContext


@Database(entities = [Class::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun classDao(): ClassDao

}
