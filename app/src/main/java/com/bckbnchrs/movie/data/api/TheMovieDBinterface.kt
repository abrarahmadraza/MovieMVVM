package com.bckbnchrs.movie.data.api

import com.bckbnchrs.movie.data.vo.MovieDetails
import com.bckbnchrs.movie.data.vo.MovieResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TheMovieDBinterface {

    @GET("movie/popular")
    fun getPopularMovie(@Query("page")page: Int) : Single<MovieResponse>


    @GET("movie/{movie_id}")
    fun getMovieDetails(@Path("movie_id") id:Int) : Single<MovieDetails>


}