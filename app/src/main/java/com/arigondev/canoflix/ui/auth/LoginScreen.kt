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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.arigondev.canoflix.R
import com.arigondev.canoflix.ui.theme.CanoFlixTheme

// 1. EL CONTENEDOR (Stateful): Conecta el ViewModel con la UI
@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val errorMenssage by viewModel.errorMenssage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LoginScreenContent(
        email = email,
        onEmailChange = { email = it },
        password = password,
        onPasswordChange = { password = it },
        errorMenssage = errorMenssage,
        isLoading = isLoading,
        onLoginClick = {
            viewModel.login(email, password) {
                onNavigateToHome() // ✅ Corregido con paréntesis para ejecutar la navegación
            }
        },
        onNavigateToRegister = onNavigateToRegister
    )
}

// LA UI PURA Stateless: 100% compatible con Previews de Android Studio
@Composable
fun LoginScreenContent(
    email: String,
    onEmailChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit,
    errorMenssage: String?,
    isLoading: Boolean,
    onLoginClick: () -> Unit,
    onNavigateToRegister: () -> Unit
) {
    //colores definidos en res/values(colors.xml)
    val netflixBlack = colorResource(id = R.color.netflix_black)
    val netflixRed = colorResource(id = R.color.netflix_red)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(netflixBlack)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            //Texto principal
            Text(
                text = stringResource(id = R.string.app_name),
                color = netflixRed,
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(32.dp))

            //campo de Email
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = {
                    Text(
                        "Correo electrónico", //  mensaje que voy a ver en correo
                        color = MaterialTheme.colorScheme.outline
                    )
                },
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

            //Campo de contraseña
            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Contraseña", color = MaterialTheme.colorScheme.outline) },
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

            //Mostrar mensaje de error si existe
            if (errorMenssage != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = errorMenssage ?: "",
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            //Boton de inicio de Sesion
            Button(
                onClick = onLoginClick,
                colors = ButtonDefaults.buttonColors(containerColor = netflixRed),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                } else {
                    Text("Iniciar Sesion", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            //boton para ir al registro
            TextButton(onClick = onNavigateToRegister) {
                Text(
                    "No tienes Cuenta? Registrate ahora!",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

//  el preview permite ver la pantalla en tiempo real en Android Studio
@androidx.compose.ui.tooling.preview.Preview(showBackground = true, backgroundColor = 0xFF141414)
@Composable
fun LoginScreenPreview() {
    CanoFlixTheme {
        LoginScreenContent(
            email = "test@canoflix.com",
            onEmailChange = {},
            password = "123456",
            onPasswordChange = {},
            errorMenssage = null,
            isLoading = false,
            onLoginClick = {},
            onNavigateToRegister = {}
        )
    }
}