package com.bckbnchrs.movie.ui.single_movie_details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bckbnchrs.movie.R
import com.bckbnchrs.movie.data.api.POSTER_BASE_URL
import com.bckbnchrs.movie.data.api.TheMovieDBClient
import com.bckbnchrs.movie.data.api.TheMovieDBinterface
import com.bckbnchrs.movie.data.repository.NetworkState
import com.bckbnchrs.movie.data.vo.MovieDetails
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_single_movie.*
import java.text.NumberFormat
import java.util.*

class SingleMovie : AppCompatActivity(){

    private lateinit var  viewModel: SingleMovieViewModel
    private  lateinit var movieRepository: MovieDetailsRepository


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_movie)

        val movieId: Int = intent.getIntExtra("id",1)

        val apiService : TheMovieDBinterface = TheMovieDBClient.getClient()
        movieRepository = MovieDetailsRepository(apiService)

        viewModel= getViewModel(movieId)

        viewModel.movieDetails.observe(this, Observer {
            bindUI(it)
        })

        viewModel.networkState.observe(this, Observer {
            progress_bar.visibility = if(it == NetworkState.LOADING) View.VISIBLE else View.GONE
            txt_error.visibility = if(it == NetworkState.ERROR) View.VISIBLE else View.GONE
        })
    }

    private fun bindUI(it : MovieDetails){
        movie_title.text = it.title
        movie_tagline.text = it.tagline
        movie_release_date.text = it.releaseDate
        movie_rating.text = it.rating.toString()
        movie_runtime.text = it.runtime.toString() + "minutes"
        movie_overview.text= it.overview

        val formatCurrency = NumberFormat.getCurrencyInstance(Locale.US)
        movie_budget.text = formatCurrency.format(it.budget)
        movie_revenue.text = formatCurrency.format(it.revenue)

        val moviePosterUrl= POSTER_BASE_URL + it.posterPath
        Glide.with(this)
            .load(moviePosterUrl)
            .into(iv_movie_poster)
    }

    private fun getViewModel(movieId: Int): SingleMovieViewModel{
        return ViewModelProvider(this, object : ViewModelProvider.Factory{

            override fun <T : ViewModel?> create(modelClass: Class<T>): T {

                return SingleMovieViewModel(movieRepository, movieId) as T

            }

        }).get(SingleMovieViewModel::class.java)
    }

}