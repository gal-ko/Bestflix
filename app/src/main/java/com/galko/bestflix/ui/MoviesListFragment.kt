package com.galko.bestflix.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.GridLayoutManager
import com.galko.bestflix.R
import com.galko.bestflix.model.MovieSearchResult
import com.galko.bestflix.network.MoviesAdapter
import com.galko.bestflix.network.OnItemClickListener
import kotlinx.android.synthetic.main.fragment_movies_list.*


class MoviesListFragment : Fragment(), OnItemClickListener {

    private val moviesAdapter = MoviesAdapter(itemClickListener = this)

    private val viewModel: MoviesViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_movies_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()

        viewModel.observeMovies(viewLifecycleOwner, Observer {
            moviesAdapter.updateItems(it)
        })
    }

    private fun initRecyclerView() {
        with(movies_list){
            //setHasFixedSize(true)
            val gridLayoutManager = GridLayoutManager(context, 3)
            gridLayoutManager.spanSizeLookup = (object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return when {
                        position % 7 == 0 || ((position + 1) % 7 == 0)  -> 2
                        adapter?.itemCount != null && position + 1 == adapter?.itemCount -> (adapter?.itemCount)?.let{
                            3 - ((position - 1) % 3)
                        } ?: 1
                        else -> 1
                    }
                }
            })
            //val staggeredGridLayoutManager = StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL)
            //staggeredGridLayoutManager.gapStrategy = GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS
            layoutManager = gridLayoutManager

            itemAnimator = DefaultItemAnimator()
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.HORIZONTAL))
            adapter = moviesAdapter
        }
    }

    override fun onItemClick(movie: MovieSearchResult) {
        viewModel.selectMovie(movie)
        //findNavController().navigate(R.id.action_MoviesListFragment_to_MovieDetailsFragment)
//        Snackbar.make(this.requireView(), "Clicked " + movie.title, Snackbar.LENGTH_LONG)
//            .setAction("Action", null).show()
    }
}