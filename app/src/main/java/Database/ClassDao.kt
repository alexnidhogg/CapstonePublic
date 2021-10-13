package Database

import DataObjects.Assignment
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import DataObjects.Class as Class1

@Dao
interface ClassDao {
    @Query("Select * FROM Class")
    fun getAll():List<DataObjects.Class>
    @Query("SELECT * FROM Class WHERE uid IN (:classIds)")
    fun loadAllByName(classIds: IntArray): List<DataObjects.Class>

    @Insert
    fun insertAll(vararg classes: DataObjects.Class)
    @Delete
    fun delete(cl: DataObjects.Class)
}