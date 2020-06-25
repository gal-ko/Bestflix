package com.galko.bestflix.ui

import android.util.Log
import androidx.lifecycle.*
import com.galko.bestflix.model.MovieDetails
import com.galko.bestflix.model.MovieSearchResult
import com.galko.bestflix.network.ListMoviesResponse
import com.galko.bestflix.network.MoviesRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MoviesViewModel : ViewModel() {

    private val movies: MutableLiveData<List<MovieSearchResult>> = MutableLiveData()
    private val selectedMovie: MutableLiveData<MovieDetails?> = MutableLiveData()
    private val repo: MoviesRepository = MoviesRepository()

    init {
        fetchPopularMovies()
    }

    private fun fetchPopularMovies() {
        repo.getPopularMovies().enqueue(object : Callback<ListMoviesResponse> {
            override fun onFailure(call: Call<ListMoviesResponse>, t: Throwable) {
                Log.e("error", t.message.toString())
                movies.postValue(emptyList())
            }

            override fun onResponse(call: Call<ListMoviesResponse>, response: Response<ListMoviesResponse>) {
                movies.postValue(response.body()?.results ?: emptyList())
            }
        })
    }

    fun observeMovies(owner: LifecycleOwner, observer: Observer<List<MovieSearchResult>>){
        movies.observe(owner, observer)
    }

    fun selectMovie(movie: MovieSearchResult?){
        if (selectedMovie.value != null){
            selectedMovie.postValue(null)
        }
        else movie?.let{
            repo.getMovieById(id = it.id)?.enqueue(object : Callback<MovieDetails> {
                override fun onFailure(call: Call<MovieDetails>, t: Throwable) {
                    Log.e("error", t.message.toString())
                    selectedMovie.postValue(null)
                }

                override fun onResponse(call: Call<MovieDetails>, response: Response<MovieDetails>) {
                    selectedMovie.postValue(response.body())
                }
            })
        }
    }

    fun observeSelectedMovie(owner: LifecycleOwner, observer: Observer<MovieDetails?>){
        selectedMovie.observe(owner, observer)
    }
}