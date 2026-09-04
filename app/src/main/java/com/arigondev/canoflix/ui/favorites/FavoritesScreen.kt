package com.arigondev.canoflix.ui.favorites

import android.util.Log.i
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.arigondev.canoflix.domain.model.FavoriteMovie

@Composable
fun FavoritesScreen(
    viewModel: FavoritesViewModel = viewModel()
) {
    val favoriteMovies by viewModel.favoriteMovies.collectAsState()
    val imageBaseUrl = "https://image.tmdb.org/t/p/w500"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF141414))
            .padding(16.dp)
    ){
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            //Titulo de la seccion
            Text(
                text = "MI LISTA *",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp).padding(bottom = 16.dp)
            )

            if (favoriteMovies.isEmpty()){
                //mensaje si no hay favoritos
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ){
                    Text(
                        text = "Aun no tienes peliculas en tu lista.\n¡Explora el catalogo y añade Algunas!",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }else{
                //Lista vertical de favoritos
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(favoriteMovies){ movie ->
                        FavoriteMovieItem(
                            movie = movie,
                            imageBaseUrl = imageBaseUrl,
                            onDelete = {viewModel.removeFavorite(movie.id)}
                            )
                    }
                }
            }
        }
    }
}

//composable para cada tarjeta de pelicula favorita
@Composable
fun FavoriteMovieItem(
    movie: FavoriteMovie,
    imageBaseUrl: String,
    onDelete: () -> Unit
){
    val posterUrl = movie.posterPath?.let { "${imageBaseUrl}$it" }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F)),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            //poster Pequeño
            AsyncImage(
                model = posterUrl,
                contentDescription = movie.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .width(90.dp)
                    .height(130.dp)
                    .clip(MaterialTheme.shapes.small)
            )

            //Informacion, Reseña y Estrellas
            Column(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = movie.title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    //Estrellas Guardadas
                    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
                        for (i in 1..5){
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = if (i <= movie.userStars) Color.Yellow else Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }

                    //Reseña del usuario
                    if (movie.userReview.isNotBlank()){
                        Text(
                            text = "\"${movie.userReview}\"",
                            color = Color.LightGray,
                            fontSize = 14.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                        )
                    }
                }

                //boton de eliminar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    IconButton(onClick = onDelete) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Eliminar",
                            tint = Color.Red
                        )
                    }
                }
            }
        }
    }
}