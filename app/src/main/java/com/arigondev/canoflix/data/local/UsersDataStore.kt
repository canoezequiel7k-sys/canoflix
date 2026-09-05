package com.arigondev.canoflix.data.local

import android.R
import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.arigondev.canoflix.domain.model.UserAccount
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first

val Context.usersDataStore by preferencesDataStore(name = "users_prefs")


class UsersDataStore(private val context: Context) {

    companion object {
        private val USERS_JSON_KEY = stringPreferencesKey("users_json_key")
        private val gson = Gson()
    }

    //Registrar un nuevo usuario(retorno true si tuvo exito, false si el correo ya existe
    suspend fun registerUser(newUser: UserAccount): Boolean {
        val currentUsers = getAllUsers()

        //verificamos si ya existe el usuario
        if (currentUsers.any { it.email.lowercase() == newUser.email.lowercase() }) {
            return false //el usuario ya existe
        }

        val updatedList = currentUsers + newUser

        context.usersDataStore.edit { preferences ->
            preferences[USERS_JSON_KEY] = gson.toJson(updatedList)
        }
        return true
    }


    //validar credenciales para el Login
    suspend fun validateUser(email: String, pass: String): UserAccount?{
        val currentUsers = getAllUsers()

        //buscamos coincidencia exacxta de email y contraseña
        return currentUsers.find{it.email.equals(email, ignoreCase = true)&& it.password == pass}
    }

    //obtener todos los usuarios registrados del JSON
    suspend fun getAllUsers(): List<UserAccount>{
        val preferences = context.usersDataStore.data.first()
        val jsonString = preferences[USERS_JSON_KEY]

        if (jsonString.isNullOrEmpty()) return emptyList()

        val type = object : TypeToken<List<UserAccount>>() {}.type
        return gson.fromJson(jsonString, type) ?: emptyList()
    }

}
