package com.arigondev.canoflix.ui.detail

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

@Composable
fun DetailScreen(
    viewModel: DetailViewModel = viewModel(),
    onNavigateBack: () -> Unit
){
    //observadores
    val movie by viewModel.movie.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isFavorite by viewModel.isFavorite.collectAsState()
    //recordatorios
    var userReview by remember { mutableStateOf("") }
    var userStars by remember { mutableIntStateOf(5) }


    val imageBaseUrl = "https://image.tmdb.org/t/p/w500"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF141414))
    ){
        if (isLoading){
            CircularProgressIndicator(
                color = Color(0xFFE50914),
                modifier = Modifier.align(Alignment.Center)
            )
        }else if (errorMessage != null){
            Text(
                text = errorMessage ?: "Error desconocido",
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }else if (movie != null) {
            val currentMovie = movie!!
            val backdropUrl = currentMovie.backdropPath?.let { "$imageBaseUrl$it" } ?: (currentMovie.posterPath?.let { "$imageBaseUrl$it" })

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ){
                //seccion superior: Imagen de fondo backdrop con boton flotante para volver atras
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                ){
                    AsyncImage(
                        model = backdropUrl,
                        contentDescription = currentMovie.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                    //boton para regresar
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier
                            .padding(16.dp)
                            .background(Color.Black.copy(alpha = 0.6f), shape = MaterialTheme.shapes.small)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "VOLVER",
                            tint = Color.White
                        )
                    }
                }


                //seccion inferior: informacion de la pelicula
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    //Titulo
                    Text(
                        text = currentMovie.title,
                        color = Color.White,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold
                    )

                    //Puntuacion y fecha de lanzamiento
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "* ${String.format("%.1f",currentMovie.voteAverage)} / 10",
                            color = Color.Yellow,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "📅 ${currentMovie.releaseDate ?: "Estreno desconocido"}",
                            color = Color.LightGray,
                            fontSize = 14.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    //Boton de reproducir(Play) corte nefli
                    Button(
                        onClick = {/*Todo: reproducir trailer en el futuro*/},
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Reprodecir",
                            tint = Color.Black
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Reproducir", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    //campo de reseña(opcional)
                    OutlinedTextField(
                        value = userReview,
                        onValueChange = { userReview = it},
                        label = {Text("Escribe una reseña...", color = Color.Gray)},
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFFE50914),
                            unfocusedBorderColor = Color.Gray,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(8.dp))


                    //Boton dinamico de "mi lista"
                    Button(
                        onClick = {
                            viewModel.toggleFavorite(userReview = userReview, userStars = userStars)
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isFavorite) Color.DarkGray else Color(0xFFE50914)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Icon(
                            imageVector = if (isFavorite) Icons.Default.Check else Icons.Default.Add,
                            contentDescription = "Mi Lista",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isFavorite) "Eliminar de Mi Lista" else "Añadir a Mi Lista",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    //sinopsis
                    Text(
                        text = "Sinopsis",
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = currentMovie.overview.ifBlank { "No hay sinopsis disponible en español."},
                        color = Color.LightGray,
                        fontSize = 15.sp,
                        lineHeight = 22.sp
                    )
                }
            }
        }
    }
}