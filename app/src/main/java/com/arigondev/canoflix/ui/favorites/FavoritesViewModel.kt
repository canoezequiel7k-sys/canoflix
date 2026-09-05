package com.arigondev.canoflix.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arigondev.canoflix.data.local.AuthDataStore
import com.arigondev.canoflix.data.local.FavoritesDataStore
import com.arigondev.canoflix.domain.model.FavoriteMovie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    private val authDataStore = AuthDataStore(application)
    private val _favoriteMovies = MutableStateFlow<List<FavoriteMovie>>(emptyList())
    val favoriteMovies: StateFlow<List<FavoriteMovie>> = _favoriteMovies.asStateFlow()

    init {
        loadUserFavorite()
    }

    private fun loadUserFavorite() {
        viewModelScope.launch {
            //cada vez que cambie el usuario logeado, cargamos sus favoritos especificos
            authDataStore.userEmailFlow.collectLatest { email ->
                val userEmail = email ?: "guest"
                val favoritesDataStore = FavoritesDataStore(getApplication(), userEmail)

                favoritesDataStore.favoritesFlow.collect{list ->
                    _favoriteMovies.value = list
                }
            }
        }
    }


    // Funcion para eliminar de favoritos
    fun removeFavorite(movieId: Int) {
        viewModelScope.launch {
            val email = authDataStore.userEmailFlow.first() ?: "guest"
            val favoritesDataStore = FavoritesDataStore(getApplication(), email)
            favoritesDataStore.removeFavorite(movieId)
        }
    }
}