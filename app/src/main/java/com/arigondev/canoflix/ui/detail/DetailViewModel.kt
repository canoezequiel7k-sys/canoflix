package com.arigondev.canoflix.ui.detail

import androidx.compose.ui.input.pointer.PointerId
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arigondev.canoflix.data.remote.MovieDto
import com.arigondev.canoflix.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

//SavedStateHandle nos permite recibir argumentos directamente desde la ruta de navegacion (NavGraph)
class DetailViewModel(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val repository = MovieRepository()

    //extraemos el movilId que vino en la URL de navegacion
    private val movieId: Int = savedStateHandle.get<String>("movieId")?.toIntOrNull() ?: 0

    private val _movie = MutableStateFlow<MovieDto?>(null)
    val movie: StateFlow<MovieDto?> = _movie.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    init {
        if (movieId != 0){
            loadMovieDetails(movieId)
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