package com.bckbnchrs.movie.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.bckbnchrs.movie.data.api.FIRST_PAGE
import com.bckbnchrs.movie.data.api.TheMovieDBinterface
import com.bckbnchrs.movie.data.vo.Movie
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class MovieDataSource(private val apiService: TheMovieDBinterface, private val compositeDisposable: CompositeDisposable) : PageKeyedDataSource<Int, Movie>() {

    private var page = FIRST_PAGE

    val networkState: MutableLiveData<NetworkState> = MutableLiveData()


    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {

        compositeDisposable.add(
            apiService.getPopularMovie(params.key)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {

                        if(it.totalPages >= params.key){
                            callback.onResult(it.moviesList, params.key+1)
                            networkState.postValue(NetworkState.LOADED)
                        }
                        else{
                            networkState.postValue(NetworkState.ENDOFLIST)
                        }
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message!!)
                    }
                )
        )

    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Movie>) {
        TODO("Not yet implemented")
    }

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Movie>
    ) {
        networkState.postValue(NetworkState.LOADING)

        compositeDisposable.add(
            apiService.getPopularMovie(page)
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        callback.onResult(it.moviesList, null, page +1)
                        networkState.postValue(NetworkState.LOADED)
                    },
                    {
                        networkState.postValue(NetworkState.ERROR)
                        Log.e("MovieDataSource", it.message!!)
                    }
                )
        )
    }

}