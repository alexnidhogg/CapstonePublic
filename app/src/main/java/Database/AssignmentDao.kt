package Database

import DataObjects.Assignment
import androidx.room.Dao
import androidx.room.Query

@Dao
interface AssignmentDao {
    @Query("Select * FROM Assignment")
    fun getAll():List<Assignment>
    @Query("SELECT * FROM Class WHERE  class_name IN (:classIds)")
    fun loadAllByName(classIds: List<String>): List<Assignment>
}