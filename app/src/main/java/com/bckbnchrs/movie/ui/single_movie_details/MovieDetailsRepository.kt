package com.bckbnchrs.movie.ui.single_movie_details

import androidx.lifecycle.LiveData
import com.bckbnchrs.movie.data.api.TheMovieDBinterface
import com.bckbnchrs.movie.data.repository.MovieDetailsNetworkDataSource
import com.bckbnchrs.movie.data.repository.NetworkState
import com.bckbnchrs.movie.data.vo.MovieDetails
import io.reactivex.disposables.CompositeDisposable

class MovieDetailsRepository(private val apiService : TheMovieDBinterface) {

    lateinit var movieDetailsNetworkDataSource : MovieDetailsNetworkDataSource

    fun fetchSingleMovieDetails (compositeDisposable: CompositeDisposable, movieId: Int): LiveData<MovieDetails>{

        movieDetailsNetworkDataSource = MovieDetailsNetworkDataSource(apiService, compositeDisposable)
        movieDetailsNetworkDataSource.fetchMovieDetails(movieId)

        return movieDetailsNetworkDataSource.downloadedMovieResponse
    }

    fun getMovieDetailsNetworkState() : LiveData<NetworkState>{
        return movieDetailsNetworkDataSource.networkState
    }
}