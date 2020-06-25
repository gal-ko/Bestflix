package com.galko.bestflix.ui

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.galko.bestflix.R
import com.galko.bestflix.model.MovieDetails
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.snackbar.Snackbar
import com.galko.bestflix.network.MoviesRepository
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bottom_sheet.*


class MainActivity : AppCompatActivity() {

    private val viewModel: MoviesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.nav_host_fragment)
        toolbar.setupWithNavController(
            navController = navController,
            configuration = AppBarConfiguration(navController.graph))

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        viewModel.observeSelectedMovie(this, Observer {
            it?.let{
                updateBottomSheet(it)
            } ?: run {
                val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottom_sheet)
                bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            }
        })

        val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
               dimmed_background.alpha = slideOffset
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_COLLAPSED){
                    viewModel.selectMovie(null)
                }
            }
        }
        )
    }

    private fun updateBottomSheet(movie: MovieDetails){
        Picasso.get().load(MoviesRepository.POSTER_PREFIX + movie.posterPostfix)
            .centerCrop()
            .fit()
            .into(bottom_sheet_poster_image)
        bottom_sheet_title.text = movie.title
        bottom_sheet_genre.text = movie.getGenreNames()
        if (movie.tagline.isNotEmpty()){
            bottom_sheet_tagline.text = "\"" + movie.tagline + "\""
            bottom_sheet_tagline.visibility = View.VISIBLE
        }
        else{
            bottom_sheet_tagline.visibility = View.GONE
        }

        bottom_sheet_plot.text = movie.overview


        val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottom_sheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    override fun onBackPressed() {
        val bottomSheetBehavior: BottomSheetBehavior<*> = BottomSheetBehavior.from(bottom_sheet)
        if (bottomSheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED){
            viewModel.selectMovie(null)
        }
        else{
            super.onBackPressed()
        }
    }
}