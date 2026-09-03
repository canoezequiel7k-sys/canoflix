package com.arigondev.canoflix.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

//esta clase se encarga de guardar el usuario, correo y exponer mediante un flow

//extencion para inicializar el dataStore de preferencias(crea un archivo llamado "auth_prefs") guarda en modmo Protocol Buffers
private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

class AuthDataStore(private val context: Context){
    companion object{
        private val IS_LOGGED_IN_KEY = booleanPreferencesKey("is_logged_in_key")
        private val USER_EMAIL_KEY = stringPreferencesKey("user_email_key")

        //Perfil y preferencias del usuario
        private val USER_NAME_KEY = stringPreferencesKey("user_name_key")
        private val USER_AVATAR_KEY = stringPreferencesKey("user_avatar_key")
        private val USER_GENRES_KEY = stringPreferencesKey("user_genres_key")
    }

    //flow que emite true o false en tiempo real segun el estado de sesion
    val isLoggedInFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences -> preferences[IS_LOGGED_IN_KEY] ?: false } //por defento false(no esta logeado)
    //para obtener el email del usuario
    val userEmailFlow: Flow<String?> = context.dataStore.data
        .map { preferences -> preferences[USER_EMAIL_KEY] }

    //Flow para observar el nombre, avatar y genero en tiempo real
    val userNameFlow: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_NAME_KEY] }
    val  userAvatarKey: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_AVATAR_KEY] }
    val userGenresKey: Flow<String?> = context.dataStore.data.map { preferences ->
        preferences[USER_GENRES_KEY] }


    //Funcion suspend para guardar la sesión cuando el usuario hace Login o Register exitoso
    suspend fun saveUserSession(email: String, isLoggedIn: Boolean){
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = isLoggedIn
            preferences[USER_EMAIL_KEY] = email
        }
    }


    //funcion para guardar las preferencias del Onboarding(nombre, avar, genero)
    suspend fun saveUserProfile(name: String, avatar: String, genres: String){
        context.dataStore.edit { preferences ->
            preferences[USER_NAME_KEY] = name
            preferences[USER_AVATAR_KEY] = avatar
            preferences[USER_GENRES_KEY] = genres
        }
    }


    //funcion suspend para cerrar sesion(Logout)
    suspend fun clearSession(){
        context.dataStore.edit { preferences ->
            preferences[IS_LOGGED_IN_KEY] = false
            preferences[USER_EMAIL_KEY] = ""
            preferences[USER_NAME_KEY] = ""
            preferences[USER_AVATAR_KEY] = ""
            preferences[USER_GENRES_KEY] = ""
        }
    }
}