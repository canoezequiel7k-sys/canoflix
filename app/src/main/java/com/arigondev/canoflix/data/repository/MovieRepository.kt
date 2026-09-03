package com.arigondev.canoflix.data.repository

import com.arigondev.canoflix.data.remote.MovieDto
import com.arigondev.canoflix.data.remote.RetrofitClient

class MovieRepository {
    //Api de TMDb
    private val apiKey = "4b60c8d51aa8ea8346d3c265af460fde"

    //obtener generos de peliculas populares
    suspend fun getPopularMovies(): List<MovieDto> {
        val response = RetrofitClient.apiService.getPopularMovies(apiKey = apiKey)
        return response.results
    }

    //obtener pelicula mejor valoradas / clasicos
    suspend fun getTopRatedMovies(): List<MovieDto> {
        val response = RetrofitClient.apiService.getTopRatedMovies(apiKey = apiKey)
        return response.results
    }

    //obtener peliculas por genero especifico (ej: 28 para Accion
    suspend fun getMoviesByGenre(genreId: String): List<MovieDto> {
        val response = RetrofitClient.apiService.getMovieByGenre(apiKey, genreId = genreId)
        return response.results
    }
}