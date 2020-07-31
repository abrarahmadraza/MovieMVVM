package com.bckbnchrs.movie.data.repository

enum class Status {
    RUNNING, SUCCESS, FAILED, ENDOFLIST
}

class NetworkState(val status: Status, val msg: String){

    companion object{
        val LOADED: NetworkState = NetworkState(Status.SUCCESS, "Success")

        val LOADING: NetworkState = NetworkState(Status.RUNNING, "Running")

        val ERROR: NetworkState = NetworkState(Status.FAILED, "Something went wrong")

        val ENDOFLIST : NetworkState = NetworkState(Status.ENDOFLIST, "You have reached the end")
    }

}