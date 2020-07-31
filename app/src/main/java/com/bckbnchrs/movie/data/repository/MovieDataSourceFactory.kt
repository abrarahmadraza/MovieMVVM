package com.bckbnchrs.movie.data.repository

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.bckbnchrs.movie.data.api.TheMovieDBinterface
import com.bckbnchrs.movie.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MovieDataSourceFactory (private val apiService: TheMovieDBinterface,private val compositeDisposable: CompositeDisposable )
    : DataSource.Factory<Int, Movie>()
{
    val moviesLiveDataSource = MutableLiveData<MovieDataSource>()


    override fun create(): DataSource<Int, Movie> {
        val movieDataSource = MovieDataSource(apiService, compositeDisposable)

        moviesLiveDataSource.postValue(movieDataSource)
        return  movieDataSource
    }


}