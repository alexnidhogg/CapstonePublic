package Database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TimeChunkDao {
    @Query("Select * From TimeChunk")
    fun getAll():List<DataObjects.TimeChunk>

    @Query("Select * From TimeChunk where obligation = (:obligation)")
    fun getByObligation(obligation : Int):List<DataObjects.TimeChunk>

    @Insert
    fun insertAll(vararg obligations: DataObjects.TimeChunk)

    @Delete
    fun delete(obligation: DataObjects.TimeChunk)
}