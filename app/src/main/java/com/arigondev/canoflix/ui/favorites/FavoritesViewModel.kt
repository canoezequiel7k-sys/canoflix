package com.arigondev.canoflix.ui.favorites

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arigondev.canoflix.data.local.FavoritesDataStore
import com.arigondev.canoflix.domain.model.FavoriteMovie
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {

    // Instanciamos nuestro DataStore local de favoritos
    private val favoritesDataStore = FavoritesDataStore(application)

    // Convertimos el Flow del DataStore en un StateFlow para la UI con collectAsState
    val favoriteMovies: StateFlow<List<FavoriteMovie>> = favoritesDataStore.favoritesFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Funcion para eliminar de favoritos
    fun removeFavorite(movieId: Int) {
        viewModelScope.launch {
            favoritesDataStore.removeFavorite(movieId)
        }
    }
}