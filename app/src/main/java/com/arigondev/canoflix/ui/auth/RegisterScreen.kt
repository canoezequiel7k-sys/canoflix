package com.arigondev.canoflix.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arigondev.canoflix.R
import com.arigondev.canoflix.ui.theme.CanoFlixTheme

//contenedor StateFUL conecta el registerViewModel con la UI
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateBackToLogin: () -> Unit,
){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val isRegistrationSuccess by viewModel.isRegistrationSuccess.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    RegisterScreenContent(
        email = email,
        onEmailChange = {email = it},
        password = password,
        onPasswordChange = {password = it},
        confirmPassword = confirmPassword,
        onConfirmPasswordChange = {confirmPassword = it},
        errorMessage = errorMessage,
        isLoading = isLoading,
        isRegistrationSuccess = isRegistrationSuccess,
        onRegisterClick = {
            viewModel.register(email, password, confirmPassword)
        },
        onNavigateBackToLogin = onNavigateBackToLogin
    )
}

//UI pura Stateless: ideal para previsualizar en el panel
@Composable
fun RegisterScreenContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    confirmPassword: String,
    onConfirmPasswordChange: (String) -> Unit,
    errorMessage: String?,
    isLoading: Boolean,
    isRegistrationSuccess: Boolean,
    onRegisterClick: () -> Unit,
    onNavigateBackToLogin: () -> Unit
){
    //colores
    val netflixBlack = colorResource(id = R.color.netflix_black)
    val netflixRed = colorResource(id = R.color.netflix_red)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(netflixBlack)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ){
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //preguntamos si el registro fue exitoso
            if (isRegistrationSuccess){
                Text(
                    text = "¡Registrado correctamente! 🎉",
                    color = Color.Gray,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onNavigateBackToLogin,
                    colors = ButtonDefaults.buttonColors(contentColor = netflixRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text("Ir a Iniciar Sesion", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }else{
                //si aun no se registro, mostramos todo el formulario normal
                //Titulo de Registro
                Text(
                    text = "Crear Cuenta",
                    color = netflixRed,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(32.dp))

                //campo Email
                OutlinedTextField(
                    value = email,
                    onValueChange = onEmailChange,
                    label = {Text("Correo electronico", color = MaterialTheme.colorScheme.outline)},
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = netflixRed,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Campo para contraseña
                OutlinedTextField(
                    value = password,
                    onValueChange = onPasswordChange,
                    label = {Text("Contraseña", color = MaterialTheme.colorScheme.outline)},
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = netflixRed,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                //Campo para confirmar Contraseña
                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = onConfirmPasswordChange,
                    label = {Text("Confirmar contraseña", color = MaterialTheme.colorScheme.outline)},
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = netflixRed,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                    ),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                //mostrar mensaje de error si existe
                if(errorMessage != null){
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }


                Spacer(modifier = Modifier.height(24.dp))

                //Boton de Registro
                Button(
                    onClick = onRegisterClick,
                    colors = ButtonDefaults.buttonColors(containerColor = netflixRed),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp),
                    enabled = !isLoading
                ) {
                    if (isLoading){
                        CircularProgressIndicator(
                            color = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }else{
                        Text("Register", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                //Volver al login
                TextButton(onClick = onNavigateBackToLogin) {
                    Text("Ya tienes cuenta? Inicia Sesion", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
    }
}

//Preview vista general
@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF141414)
@Composable
fun RegisterScreenPreview() {
    CanoFlixTheme {
        RegisterScreenContent(
            email = "nuevo@canoflix.com",
            onEmailChange = {},
            password = "123456",
            onPasswordChange = {},
            confirmPassword = "123456",
            onConfirmPasswordChange = {},
            errorMessage = null,
            isLoading = false,
            isRegistrationSuccess = true,
            onRegisterClick = {},
            onNavigateBackToLogin = {}
        )
    }
}




