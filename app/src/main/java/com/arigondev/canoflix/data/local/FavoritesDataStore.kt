package com.arigondev.canoflix.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.arigondev.canoflix.domain.model.FavoriteMovie
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlin.collections.emptyList

val Context.favoritesDataStore by preferencesDataStore(name = "favorites_prefs")

class FavoritesDataStore(private val context: Context) {
    companion object{
        private val FAVORITES_JSON_KEY = stringPreferencesKey("favorites_json_key")
        private val gson = Gson()
    }

    //flow que emite la lista de favoritos leidos desde el JSON
    val favoritesFlow: Flow<List<FavoriteMovie>> = context.favoritesDataStore.data
        .map{ preferences ->
            val jsonString = preferences[FAVORITES_JSON_KEY]
            if (jsonString.isNullOrEmpty()){
                emptyList()
            }else{
                //convertimos el Json de vuelta a una lista de objetos con Gson TypeToken
                val type = object : TypeToken<List<FavoriteMovie>>() {}.type
                gson.fromJson(jsonString, type) ?: emptyList()
            }
        }

    //añadir o actualizar una pelicula en favoritos
    suspend fun addFavorite(movie: FavoriteMovie){
        context.favoritesDataStore.edit { preferences ->
            val currentList = getFavoritesCurrentList(preferences[FAVORITES_JSON_KEY])

            //si ya existe, remplazamos por si se actualizo la reseña o estrella
            val updatedList = currentList.filter {it.id != movie.id} + movie

            //convertimos la lista completa a JSON con Gson y la guardamos
            preferences[FAVORITES_JSON_KEY] = gson.toJson(updatedList)
        }
    }

    //eliminar una pelicula de favoritos por su ID
    suspend fun removeFavorite(movieId: Int){
        context.favoritesDataStore.edit { preferences ->
            val currentList = getFavoritesCurrentList(preferences[FAVORITES_JSON_KEY])
            val updatedList = currentList.filter {it.id != movieId}
            preferences[FAVORITES_JSON_KEY] = gson.toJson(updatedList)
        }
    }


    //funcion auxiliar privada para pasar el Json Actual
    private fun getFavoritesCurrentList(jsonString: String?) : List<FavoriteMovie>{
        if(jsonString.isNullOrEmpty()) return emptyList()
        val type = object : TypeToken<List<FavoriteMovie>>() {}.type
        return gson.fromJson(jsonString, type) ?: emptyList()
    }

}