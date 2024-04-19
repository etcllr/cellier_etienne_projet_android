package fr.nextu.cellier_etienne.entity

data class MovieEntity(
    var id: Int,
    var title: String,
    var tmdb_id: Int,
    var imdb_id: String,
    var followers: Int,
    var production_year: Int,
    var poster: String
)