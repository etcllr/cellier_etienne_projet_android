package fr.nextu.cellier_etienne.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie")
data class MovieEntity(
    @PrimaryKey var id: Int,
    @ColumnInfo(name = "title") var title: String,
    @ColumnInfo(name = "tmdb_id") var tmdb_id: Int?,
    @ColumnInfo(name = "imdb_id") var imdb_id: String?,
    @ColumnInfo(name = "followers") var followers: Int?,
    @ColumnInfo(name = "production_year") var production_year: Int?
) {
    @ColumnInfo(name = "poster", defaultValue = "")
    var poster: String? = ""
        set(value) {
            field = value ?: ""
        }
}