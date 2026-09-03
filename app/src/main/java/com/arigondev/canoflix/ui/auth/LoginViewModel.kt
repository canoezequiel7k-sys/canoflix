package com.arigondev.canoflix.ui.auth

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arigondev.canoflix.R
import com.arigondev.canoflix.data.local.AuthDataStore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {
    //instancia de nuestro data store usando el contexto de la aplicacion
    private val authDataStore = AuthDataStore(application)

    //estado UI para manejar errores o mensaje en pantalla
    private val _errorMenssage = MutableStateFlow<String?>(null)
    val errorMenssage: StateFlow<String?> = _errorMenssage.asStateFlow()

    //estado para saber si esta cargando(por ejemplo, al hacer clic en el boton)
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()


    //funcion Login
    fun login(email: String, pass: String, onLoginSuccess: () -> Unit) {
        val context = getApplication<Application>()

        //validaciones basicas
        if (email.isBlank() || pass.isBlank()) {
            _errorMenssage.value = context.getString(R.string._errorMessage)
            return
        }
        if (!email.contains("@")) {
            _errorMenssage.value = context.getString(R.string._errorMensageEmail)
            return
        }

        //Si pasa las validaciones, lanzamos una corrutina en el viewModelScope
        viewModelScope.launch {
            _isLoading.value = true
            _errorMenssage.value = null


            //El try ejecuta la acción
            try {
                //simulamos una pequeña áusa de red(opcional, le da toque profesional
                kotlinx.coroutines.delay(500)

                //guardamos la sesion en dataStore Preferences
                authDataStore.saveUserSession(email = email, isLoggedIn = true)

                //ejecutamos el callback de exito para navegar a la pantalla principal
                onLoginSuccess()
            //catch evita que la app muera si hay un error
            } catch (e: Exception) {
                _errorMenssage.value = "${context.getString(R.string._errorMensageLogin)} ${e.localizedMessage}"
            //finally limpia los estados (como apagar el indicador de carga) sin importar el resultado
            } finally {
                _isLoading.value = false
            }
        }
    }
}
