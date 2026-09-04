package com.arigondev.canoflix.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arigondev.canoflix.data.remote.MovieDto
import com.arigondev.canoflix.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val repository = MovieRepository()

    //estado para las diferentes categorias de peliculas
    private val _popularMovies = MutableStateFlow<List<MovieDto>>(emptyList())
    val popularMovies: StateFlow<List<MovieDto>> = _popularMovies.asStateFlow()

    private val _topRatedMovies = MutableStateFlow<List<MovieDto>>(emptyList())
    val topRatedMovies: StateFlow<List<MovieDto>> = _topRatedMovies.asStateFlow()

    private val _actionMovies = MutableStateFlow<List<MovieDto>>(emptyList())
    val actionMovies : StateFlow<List<MovieDto>> = _actionMovies.asStateFlow()

    private val _terrorMovies = MutableStateFlow<List<MovieDto>>(emptyList())
    val terrorMovies : StateFlow<List<MovieDto>> = _terrorMovies.asStateFlow()

    private val _animationMovies = MutableStateFlow<List<MovieDto>>(emptyList())
    val animationMovies : StateFlow<List<MovieDto>> = _animationMovies.asStateFlow()


    //Estados de carga y Errores
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        //al abrir la pantalla de home, cargamos todas las categorias automaticamente
        loadHomeData()

    }

    private fun loadHomeData() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null


            try {
                //llamamos a las 3 peticiones en paralelo/secuencia
                _popularMovies.value = repository.getPopularMovies()
                _topRatedMovies.value = repository.getTopRatedMovies()
                _actionMovies.value = repository.getMoviesByGenre(genreId = "28") //28 es Aacion en TMDb
                _terrorMovies.value = repository.getMoviesByGenre(genreId = "27") //27 es Terror eb TMDb
                _animationMovies.value = repository.getMoviesByGenre(genreId = "16") //16 es Animada eb TMDb
            }catch (e: Exception){
                _errorMessage.value = "Error al cargar peliculas: ${e.localizedMessage}"
            }finally {
                _isLoading.value = false
            }
        }
    }
}