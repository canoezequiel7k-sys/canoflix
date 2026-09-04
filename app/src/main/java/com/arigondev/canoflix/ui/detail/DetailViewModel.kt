package com.arigondev.canoflix.ui.detail

import android.app.Application
import androidx.compose.ui.input.pointer.PointerId
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arigondev.canoflix.data.local.FavoritesDataStore
import com.arigondev.canoflix.data.remote.MovieDto
import com.arigondev.canoflix.data.repository.MovieRepository
import com.arigondev.canoflix.domain.model.FavoriteMovie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

//SavedStateHandle nos permite recibir argumentos directamente desde la ruta de navegacion (NavGraph)
class DetailViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle
): AndroidViewModel(application) {
    private val repository = MovieRepository()
    private val favoritesDataStore = FavoritesDataStore(application)

    //extraemos el movilId que vino en la URL de navegacion
    private val movieId: Int = savedStateHandle.get<String>("movieId")?.toIntOrNull() ?: 0

    private val _movie = MutableStateFlow<MovieDto?>(null)
    val movie: StateFlow<MovieDto?> = _movie.asStateFlow()

    //estado para saber si esta pelicula ya es fav
    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    init {
        if (movieId != 0){
            loadMovieDetails(movieId)
            checkIfFavorite(movieId)
        }
    }

    private fun checkIfFavorite(id: Int) {
        viewModelScope.launch {
            favoritesDataStore.favoritesFlow.collectLatest { favoritesList ->
                _isFavorite.value = favoritesList.any{it.id == id}
            }
        }
    }

    //boton para Añadir o quitar "Mi lista"
    fun toggleFavorite(userReview: String = "", userStars: Int = 0){
        val currentMovie = _movie.value ?: return

        viewModelScope.launch {
            if (_isFavorite.value){
                //si ya era favorita, la eliminamos
                favoritesDataStore.removeFavorite(currentMovie.id)
            }else{
                //si no era favorita, la guardamos con su reseña Y estrella
                val favoriteMovie = FavoriteMovie(
                    id = currentMovie.id,
                    title = currentMovie.title,
                    posterPath = currentMovie.posterPath,
                    overview = currentMovie.overview,
                    voteAverage = currentMovie.voteAverage,
                    userReview = userReview,
                    userStars = userStars
                )
                favoritesDataStore.addFavorite(favoriteMovie)
            }
        }
    }

    private fun loadMovieDetails(id: Int) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                _movie.value = repository.getMovieDetails(movieId = id)
            }catch (e: Exception){
                _errorMessage.value = "Error al cargar detalles: ${e.localizedMessage}"
            }finally {
                _isLoading.value = false
            }
        }
    }
}