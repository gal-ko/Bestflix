package com.galko.bestflix.network

import com.galko.bestflix.model.MovieDetails
import com.galko.bestflix.model.MovieSearchResult
import com.squareup.moshi.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


class MoviesRepository {

    companion object {
        const val API_KEY = "3218c91ef478f6b750037c791b3e5c52"
        const val BASE_URL = "https://api.themoviedb.org/"
        const val POSTER_PREFIX = "https://image.tmdb.org/t/p/w400"
    }

    val api : ImdbApi
    get() {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create())
            .build().create(ImdbApi::class.java)
    }

    fun getPopularMovies() = api.getPopularMovies()

    fun getMovieById(id: String) = api.getMovieById(id = id)

}

interface ImdbApi {
    @GET("3/movie/popular")
    fun getPopularMovies(
        @Query("api_key") apikey: String = MoviesRepository.API_KEY,
        @Query("language") language: String = "en-US",
        @Query("page") page: String = "1"): Call<ListMoviesResponse>

    @GET("3/movie/{id}")
    fun getMovieById(
        @Path("id") id: String,
        @Query("api_key") apikey: String = MoviesRepository.API_KEY,
        @Query("language") language: String = "en-US"): Call<MovieDetails>?
}

data class ListMoviesResponse(
    @field:Json(name = "results") val results : List<MovieSearchResult>
)