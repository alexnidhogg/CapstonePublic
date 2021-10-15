package Database

import DataObjects.Assignment
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AssignmentDao {
    @Query("Select * FROM Assignment")
    fun getAll():List<Assignment>
    @Query("SELECT * FROM Assignment WHERE  class_name = :classIds")
    fun loadAllByName(classIds: String): List<Assignment>
    @Insert
    fun insertAll(vararg assignments: DataObjects.Assignment)
}