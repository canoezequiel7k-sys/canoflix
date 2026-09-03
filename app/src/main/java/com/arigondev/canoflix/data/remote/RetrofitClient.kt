package com.arigondev.canoflix.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
//aca vamos a configurar el motor de red con OkHttp y Retofit

//object define un Singleton, una unica instancia en toda la aplicacion, para no recrear xonexiones htto innecesariamente
object RetrofitClient {
    private const val BASE_URL = "https://api.themovieb.org/3/"

    //creamos el interceptor para ver los logs de red en el Logcat
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    //creamos el cliente OkHttpClient y le añadimos el interceptor
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .build()


    //instancia perezosa (Lazy) de Retrofit
    val apiService: MovieApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(MovieApiService::class.java)
    }
}