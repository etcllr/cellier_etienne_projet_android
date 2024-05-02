package fr.nextu.cellier_etienne

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.nextu.cellier_etienne.dao.MovieDAO
import fr.nextu.cellier_etienne.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {

    abstract fun movieDao(): MovieDAO

    companion object {
        fun getInstance(applicationContext: Context): AppDatabase {
            return Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java, "project_kotlin.db"
            ).fallbackToDestructiveMigration().build()
        }
    }
}