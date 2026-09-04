package com.arigondev.canoflix.ui.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.arigondev.canoflix.data.remote.MovieDto
import com.arigondev.canoflix.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel : ViewModel() {
    private val repository = MovieRepository()

    //Estado para guardar el texto que el usuario escribe en el search
    private val _searchQuery = MutableStateFlow("")
    val searchQuery : StateFlow<String> = _searchQuery.asStateFlow()

    //Estado para guardar la lista de peliculas encontradas
    private val _searchResults = MutableStateFlow<List<MovieDto>>(emptyList())
    val searchResults: StateFlow<List<MovieDto>> = _searchResults.asStateFlow()


    //Estados de carga y error
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()


    //funcion que se ejecuta cada vez que el usuario escribe en la barra de busqueda
    fun onSearchQueryChanged(query: String){
        _searchQuery.value = query

        //Si la barra esta vacia, limpiamos los resultados y salimos
        if (query.isBlank()){
            _searchResults.value = emptyList()
            return
        }

        //Si hay texto, lanzamos la busqueda a la API
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                //Llamamos a nuestro repositorio con el texto ingresado
                _searchResults.value = repository.searchMovies(query)
            }catch (e: Exception){
                _errorMessage.value = "Error al buscar: ${e.localizedMessage}"
            }finally {
                _isLoading.value = false
            }
        }
    }
}