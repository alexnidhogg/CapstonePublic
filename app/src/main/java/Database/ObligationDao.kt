package Database

import DataObjects.Obligation
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ObligationDao {
    @Query("Select * From Obligation")
    fun getAll():List<Obligation>

    @Insert
    fun insertAll(vararg obligations: DataObjects.Obligation)

    @Delete
    fun delete(obligation: DataObjects.Obligation)
}