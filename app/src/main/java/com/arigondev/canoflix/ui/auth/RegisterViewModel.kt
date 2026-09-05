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

class RegisterViewModel(application: Application) : AndroidViewModel(application) {
    private val authDataStore = AuthDataStore(application)

    //Estado para manejar mensajes de error en pantalla
    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    //estado para saber que el registro fue exotoso
    private val _isRegistrationSuccess = MutableStateFlow(false)
    val isRegistrationSuccess: StateFlow<Boolean> = _isRegistrationSuccess.asStateFlow()

    //funcion de registro
    fun register(email: String, pass: String, confirmPass: String) {
        val context = getApplication<Application>()
        val usersDataStore = com.arigondev.canoflix.data.local.UsersDataStore(context)

        //Validaciones basicas
        if (email.isBlank() || pass.isBlank() || confirmPass.isBlank()) {
            _errorMessage.value = context.getString(R.string._errorMessage)
            return
        }
        if (!email.contains("@")) {
            _errorMessage.value = context.getString(R.string._errorMensageEmail)
            return
        }
        if (pass != confirmPass) {
            _errorMessage.value = "Las contraseñas no coinciden"
            return
        }
        if (pass.length < 6) {
            _errorMessage.value = "La contraseña debe tener al menos 6 caracteres"
            return
        }


        //lanzamos corrutina para guardar de forma asincrona
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                kotlinx.coroutines.delay(600) //simulamos tiempo de red

                //creamos el objeto con la cuenta del usuario
                val newUser = com.arigondev.canoflix.domain.model.UserAccount(email = email, password = pass)
                //intentamos registrarlo en la BD Json
                val isRegistered = usersDataStore.registerUser(newUser)

                if (isRegistered){
                    // ¡Éxito! Activamos la bandera, SIN iniciar sesión automáticamente
                    _isRegistrationSuccess.value = true
                }else{
                    //si el correo ya estaba en uso
                    _errorMessage.value = "El correo electronico ya esta registrado"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error al registrarse: ${e.localizedMessage}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}