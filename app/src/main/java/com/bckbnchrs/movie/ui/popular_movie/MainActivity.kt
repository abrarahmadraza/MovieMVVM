package com.bckbnchrs.movie.ui.popular_movie

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.bckbnchrs.movie.R
import com.bckbnchrs.movie.data.api.TheMovieDBClient
import com.bckbnchrs.movie.data.api.TheMovieDBinterface
import com.bckbnchrs.movie.data.repository.NetworkState
import com.bckbnchrs.movie.ui.single_movie_details.SingleMovie
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MainActivityViewModel

    lateinit var movieRepository: MoivePagedListRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val apiService: TheMovieDBinterface = TheMovieDBClient.getClient()

        movieRepository = MoivePagedListRepository(apiService)

        viewModel = getViewModel()

        val movieAdapter = PopularMoviePagedListAdapter(this)

        val gridLayoutManager = GridLayoutManager(this,3)

        gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup(){
            override fun getSpanSize(position: Int): Int {
                val viewType = movieAdapter.getItemViewType(position)
                return if(viewType == movieAdapter.MOVIE_VIEW_TYPE) 1
                else 3
            }
        }

        rv_movie_list.layoutManager = gridLayoutManager
        rv_movie_list.setHasFixedSize(true)
        rv_movie_list.adapter = movieAdapter

        viewModel.moviePagedList.observe(this, Observer {
            movieAdapter.submitList(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar_popular.visibility = if(viewModel.listIsEmpty() && it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error_popular.visibility = if(viewModel.listIsEmpty() && it == NetworkState.ERROR) View.VISIBLE else View.GONE

            if(!viewModel.listIsEmpty()){
                movieAdapter.setNetworkState(it)
            }

        })
        }

    private fun getViewModel() : MainActivityViewModel{
        return ViewModelProvider(this, object: ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                return MainActivityViewModel(movieRepository) as T
            }
        }).get(MainActivityViewModel::class.java)


    }
}