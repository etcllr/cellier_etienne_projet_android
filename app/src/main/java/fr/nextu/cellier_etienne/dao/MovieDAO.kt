package fr.nextu.cellier_etienne.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.nextu.cellier_etienne.entity.MovieEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDAO {

    @Query("SELECT * FROM movie")
    fun getAll(): List<MovieEntity>

    @Query("SELECT * FROM movie WHERE id = :id")
    fun getById(id: Int): MovieEntity

    @Query("SELECT * FROM movie WHERE title = :title")
    fun getByTitle(title: String): MovieEntity

    @Query("SELECT * FROM movie")
    fun getFlowData(): Flow<List<MovieEntity>>

    @Query("SELECT * FROM movie WHERE id IN (:ids)")
    fun getByIds(ids: IntArray): List<MovieEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg movies: MovieEntity)

    @Delete
    fun delete(movie: MovieEntity)
}