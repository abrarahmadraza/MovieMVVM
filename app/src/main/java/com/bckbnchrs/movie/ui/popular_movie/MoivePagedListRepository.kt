package com.bckbnchrs.movie.ui.popular_movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.bckbnchrs.movie.data.api.POST_PER_PAGE
import com.bckbnchrs.movie.data.api.TheMovieDBinterface
import com.bckbnchrs.movie.data.repository.MovieDataSource
import com.bckbnchrs.movie.data.repository.MovieDataSourceFactory
import com.bckbnchrs.movie.data.repository.NetworkState
import com.bckbnchrs.movie.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable

class MoivePagedListRepository(private val apiService: TheMovieDBinterface) {

    lateinit var  moviePagedList : LiveData<PagedList<Movie>>
    lateinit var moviesDataSourceFactory: MovieDataSourceFactory

    fun fetchLiveMoviePagedList(compositeDisposable: CompositeDisposable)
    :LiveData<PagedList<Movie>>
    {

        moviesDataSourceFactory = MovieDataSourceFactory(apiService, compositeDisposable)

        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setPageSize(POST_PER_PAGE)
            .build()

        moviePagedList = LivePagedListBuilder(moviesDataSourceFactory, config).build()
        return moviePagedList
    }


    fun getNetworkState(): LiveData<NetworkState>{
        return Transformations.switchMap<MovieDataSource, NetworkState>(
            moviesDataSourceFactory.moviesLiveDataSource, MovieDataSource::networkState
        )
    }

}