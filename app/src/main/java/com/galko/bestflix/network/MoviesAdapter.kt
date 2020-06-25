package com.galko.bestflix.network

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.galko.bestflix.R
import com.galko.bestflix.model.MovieSearchResult
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.view_movie_list_item.view.*


class MoviesAdapter(private val movies: MutableList<MovieSearchResult> = mutableListOf(),
                    private val itemClickListener: OnItemClickListener
) :
    RecyclerView.Adapter<MoviesAdapter.MyViewHolder>() {

    private val picasso: Picasso = Picasso.get()

    class MyViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.view_movie_list_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        with(holder.view){
            Log.d("galko", movies[position].toString())
            val movie = movies[position]
            movie_name.text = movie.title
            picasso.load(MoviesRepository.POSTER_PREFIX + movie.posterPostfix)
                .fit()
                //.resize(500, 250)
                .centerCrop()
                //.centerCrop()
                //.fit()
                .into(movie_poster, object : Callback {
                    override fun onSuccess() { movie_poster_gradient.visibility = View.VISIBLE }
                    override fun onError(e: Exception?) { movie_poster_gradient.visibility = View.GONE }
                })
            setOnClickListener {
                itemClickListener.onItemClick(movie)
            }
        }

    }

    fun updateItems(newMovies: List<MovieSearchResult>){
        movies.clear()
        movies.addAll(newMovies)
        notifyDataSetChanged()
    }

    override fun getItemCount() = movies.size
}

interface OnItemClickListener {
    fun onItemClick(movie: MovieSearchResult)
}