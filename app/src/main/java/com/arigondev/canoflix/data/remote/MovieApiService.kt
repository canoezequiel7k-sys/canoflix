package com.arigondev.canoflix.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    //endpoint para obtener peliculas populares
    @GET("movie/popular")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): MovieResponse

    //endpoint para obtener mejor pelicula valorada(clasicas/toprates)
    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): MovieResponse

    //endpoint para obtener peliculafiltrada por genero(Accion, terro, etc)
    @GET("discover/movie")
    suspend fun getMovieByGenre(
        @Query("api_key") apiKey: String,
        @Query("with_genres") genreId: String, //ID del genero. ej: 28 = accion
        @Query("language") language: String = "es-ES"
    ): MovieResponse
}