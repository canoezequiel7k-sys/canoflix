package com.arigondev.canoflix.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.arigondev.canoflix.ui.auth.LoginScreen
import com.arigondev.canoflix.ui.auth.RegisterScreen
import com.arigondev.canoflix.ui.home.HomeScreen


//Definimos las rutas (id de texto para cada pantalla)
object Screen {
    const val LOGIN = "login_screen"
    const val REGISTER = "register_screen"
    const val HOME = "home_screen"
}

@Composable
fun CanoFlixNavGraph(
    navController: NavHostController = rememberNavController()
){
    //NavHost gestiona que pantalla se muestra segun la ruta actual
    NavHost(
        navController = navController,
        startDestination = Screen.LOGIN //pantalla inicial al abrir la app
    ){
        //Ruta del login
        composable (route = Screen.LOGIN){
            LoginScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.HOME){
                        popUpTo(Screen.LOGIN){inclusive = true} // Borra el login del historial
                    }
                },
                onNavigateToRegister = {
                    navController.navigate(Screen.REGISTER) //Salta a la pantalla de register
                }
            )
        }


        //Ruta del Registro
        composable(route = Screen.REGISTER){
            RegisterScreen(
                onNavigateToHome = {
                    navController.navigate(Screen.HOME){
                        popUpTo(Screen.LOGIN){inclusive = true}
                    }
                },
                onNavigateBackToLogin = {
                    navController.popBackStack() //Vuelve a la pantalla anterior LOGIM
                }
            )
        }
    //Ruta del HOME catalogo de peliculas - temporal por ahora
        composable (route = Screen.HOME){
            HomeScreen()
        }
    }
}

