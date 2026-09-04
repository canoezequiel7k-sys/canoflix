package com.arigondev.canoflix.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.arigondev.canoflix.data.remote.MovieDto
import com.arigondev.canoflix.ui.theme.NetflixLightGray

@Composable
fun SearchScreen(
    onMovieClick: (Int) -> Unit, //para navegar al detalle cuando toquen una pelicula encontrada
    viewModel: SearchViewModel = viewModel()
){
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searchResults by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val imageBaseUrl = "https://image.tmdb.org/t/p/w500"

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            //Texto de la Seccion
            Text(
                text = "Buscar \uD83D\uDD0D",
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
                //modifier = Modifier.padding(bottom = 12.dp)
            )

            //Barra de busqueda(textFilde)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = {query ->
                    viewModel.onSearchQueryChanged(query) //cada letra que escribe se dispara a la busqueda
                },
                placeholder = {
                    Text("Buscar peliculas...",
                        color = NetflixLightGray)},
                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = NetflixLightGray)},
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color.Gray,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            //Contenido segun el estado
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ){
                if (isLoading){
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }else if (errorMessage != null){
                    Text(
                        text = errorMessage ?: "",
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }else if (searchQuery.isBlank()){
                    //Mensaje inicial antes de buscar
                    Text(
                        text = "Escribe el nombre de una pelicula para comenzar...",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }else if (searchResults.isEmpty()){
                    //Mensaje si no hay resultados
                    Text(
                        text = "No se encuentra peliculas para \"$searchQuery\"",
                        color = Color.LightGray,
                        fontSize = 16.sp,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }else{
                    //Grille vertical de dos resultados(Columnas)
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(searchResults){movie ->
                            SearchMovieItem(movie = movie, imageBaseUrl = imageBaseUrl){onMovieClick(movie.id)}
                        }
                    }
                }
            }
        }
    }
}

//composable individual para cada celda de pelicula en lña busqueda
@Composable
fun SearchMovieItem(
    movie: MovieDto,
    imageBaseUrl: String,
    onClick: () -> Unit
){
    val posterUrl = movie.posterPath?.let {"$imageBaseUrl$it"}

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .clickable{onClick()},
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        AsyncImage(
            model = posterUrl,
            contentDescription = movie.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}