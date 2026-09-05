package com.arigondev.canoflix.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.arigondev.canoflix.data.local.AuthDataStore
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application): AndroidViewModel(application) {

    private val authDataStore = AuthDataStore(application)

    //exponemos el email del usuario logueado en tiempo real
    val userEmail: StateFlow<String?> = authDataStore.userEmailFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = "Cargando..."
    )

    //funcion para cerrar sesion(Limpia el DataStore)
    fun logout(onLogoutComplete: () -> Unit){
        viewModelScope.launch {
            authDataStore.clearSession()
            onLogoutComplete()
        }
    }
}