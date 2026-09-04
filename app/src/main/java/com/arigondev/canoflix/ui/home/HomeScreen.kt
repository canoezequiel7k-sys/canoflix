package com.arigondev.canoflix.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.arigondev.canoflix.data.remote.MovieDto

@Composable
fun HomeScreen(
    //recibe el ID de la pelicula
    onMovieClick: (Int) -> Unit, 
    viewModel: HomeViewModel = viewModel()
){
    //observamos los StateFlows de nuestro HomeViewModel
    val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

    val popularMovies by viewModel.popularMovies.collectAsState()
    val topRatedMovies by viewModel.topRatedMovies.collectAsState()
    val actionMovies by viewModel.actionMovies.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val terrorMovies by viewModel.terrorMovies.collectAsState()
    val animationMovies by viewModel.animationMovies.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background) //fondo negro estilo netflix
    ){
        if (isLoading) {
            //indicador de carga centrado
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.background, //rojo netflix
                modifier = Modifier.align(Alignment.Center)
            )
        }else if (errorMessage != null){
            //mensaje de error si falla la red
            Text(
                text = errorMessage ?: "Error desconocido",
                color = Color.Red,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(16.dp)
            )
        }else{
            //contenido principal: Scroll vertical con secciones estilo Netflix
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                //titulo de la app/ header Superior
                item {
                    Text(
                        text = "CANOFLIX",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp, top = 24.dp)
                    )
                }

                //Fila 1 Peliculas populares
                item {
                    MovieRow(
                        title = "Tendencias / Populares",
                        movies = popularMovies,
                        imageBaseUrl = IMAGE_BASE_URL,
                        onMovieClick = onMovieClick)
                }

                //fila 2 Peliculas clasicas
                item {
                    MovieRow(
                        title = "Clasicos",
                        movies = topRatedMovies,
                        imageBaseUrl = IMAGE_BASE_URL,
                        onMovieClick = onMovieClick)
                }

                //fila 3 Peliculas de Accion
                item {
                    MovieRow(
                        title = "Accion / Adrenalina",
                        movies = actionMovies,
                        imageBaseUrl = IMAGE_BASE_URL,
                        onMovieClick = onMovieClick)
                }

                //fila 4 Peliculas de Terror
                item {
                    MovieRow(
                        title = "Terror / Suspenso",
                        movies = terrorMovies,
                        imageBaseUrl = IMAGE_BASE_URL,
                        onMovieClick = onMovieClick)
                }

                //fila 5 Peliculas Animadas
                item {
                    MovieRow(
                        title = "Animadas / Dibujos",
                        movies = animationMovies,
                        imageBaseUrl = IMAGE_BASE_URL,
                        onMovieClick = onMovieClick)
                }
            }
        }
    }
}

//Composable reutilizable para cada fila horizontal de peliculas
@Composable
fun MovieRow(title: String,
             movies: List<MovieDto>,
             imageBaseUrl : String,
             onMovieClick: (Int) -> Unit){
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        //titulo de la categpria
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp).padding(bottom = 8.dp)
        )

        //carrusel horizontal de posters
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(movies){movie ->
                MovieItem(
                    movie = movie,
                    imageBaseUrl = imageBaseUrl,
                    onClick = {onMovieClick(movie.id)})
            }
        }
    }
}


//composable individual para cada poster de pelicula
@Composable
fun MovieItem(movie:
              MovieDto,
              imageBaseUrl: String,
              onClick: () -> Unit){
    val posterUrl = movie.posterPath?.let { "$imageBaseUrl$it" }

    Card(
        modifier = Modifier
            .width(130.dp)
            .height(190.dp)
            .clickable{onClick()},
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = Color.DarkGray)
    ) {
        AsyncImage(
            model = posterUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}