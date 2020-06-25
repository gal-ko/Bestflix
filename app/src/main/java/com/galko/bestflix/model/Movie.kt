package com.galko.bestflix.model

import com.squareup.moshi.Json
import java.lang.StringBuilder


data class MovieSearchResult (
    @field:Json(name = "id") val id: String,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "poster_path") val posterPostfix: String,
    @field:Json(name = "overview") val overview: String,
    @field:Json(name = "release_date") val release_date: String
)

data class MovieDetails(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "poster_path") val posterPostfix: String,
    @field:Json(name = "overview") val overview: String,
    @field:Json(name = "release_date") val release_date: String,

    @field:Json(name = "tagline") val tagline: String,
    @field:Json(name = "budget") val budget: String,
    @field:Json(name = "runtime") val runtime: String,
    @field:Json(name = "genres") val genres: List<Genre>
){
    fun getGenreNames() : String{
        val builder = StringBuilder()
        genres.forEachIndexed { index, genre ->
            builder.append(genre.name)
            if (index != genres.size - 1){
                builder.append(", ")
            }
        }
        return builder.toString()
    }
}

data class Genre(
    @field:Json(name = "id") val id: String,
    @field:Json(name = "name") val name: String
)