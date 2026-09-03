Architecture: 
|
|-> data (Capa de datos y acceso externo/local):
◦  |
   |->  data.remote (Aquí pondremos las llamadas a la API con Retrofit y los DTOs)
◦  |
   |->  data.local (Aquí irá Room Database para cuando guardemos favoritos)
◦  |
   |->  data.repository (Aquí implementaremos los repositorios)
|
|-> domain (Capa de negocio / reglas puras de Kotlin):
◦  | 
   |->  domain.model (Modelos de datos limpios para la UI)
◦  |
   |->  domain.repository (Interfaces que definen qué datos se pueden pedir)
|
|-> ui (Capa de presentación y vistas con Compose):
◦  |
   |->  ui.home (Pantalla principal: Catálogo estilo Netflix)
◦  |
   |->  ui.detail (Pantalla de detalles de la película)
◦  |
   |->  ui.favorites (Pantalla "Mi Lista")
◦  |
   |->  ui.navigation (Grafo de navegación)
◦  |
   |->  ui.theme (Ya existe con los colores y temas)


#Como funciona el JSON de TMDb:
{
    "page": 1,
    "results": [
    {
        "id": 123,
        "title": "Nombre de la película",
        "overview": "Sinopsis de la película...",
        "poster_path": "/ruta_del_poster.jpg",
        "vote_average": 8.5,
        "release_date": "2026-01-01"
        }
    ] ,
    "total_pages": 500,
    "total_results": 10000
}"



[App Start]
│
├─► ¿Está logueado? (Verificamos con DataStore)
│     ├── NO ──► [LoginScreen] ──(¿No tienes cuenta?)──► [RegisterScreen]
│     │               │ (Login exitoso)
│     │               ▼
│     └── SÍ ──► [HomeScreen] (Catálogo de Películas Netflix)
│                     │
│                     ├──► [DetailScreen] (Detalle de película)
│                     └──► [FavoritesScreen] ("Mi Lista")