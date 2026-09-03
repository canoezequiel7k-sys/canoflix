package com.arigondev.canoflix.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {
    //Endpoint para obtener peliculas populares
    //url final: https://api.themoviedb.org/3/movie/popular?api_key=4b60c8d51aa8ea8346d3c265af460fde
    //Hace que la función sea una corrutina de Kotlin, asi podemos llamarla sin bloquear el hilo principal
    suspend fun getPopularMovies(
        // Añade automaticamente parametros dinamicos a la URL
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): MovieResponse
}