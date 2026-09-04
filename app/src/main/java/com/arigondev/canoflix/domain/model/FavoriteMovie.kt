package com.arigondev.canoflix.domain.model

data class FavoriteMovie(
    val id: Int,
    val title: String,
    val posterPath: String?,
    val overview: String,
    val voteAverage: Double,
    val userReview: String = "", //comentario o reseña del usuario
    val userStars: Int = 0  //Puntuacion de estrellas, del 1 al 5
)
