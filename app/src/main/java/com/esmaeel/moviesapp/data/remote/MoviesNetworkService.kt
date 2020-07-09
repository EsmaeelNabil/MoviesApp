package com.esmaeel.moviesapp.data.remote

import com.esmaeel.moviesapp.data.models.PopularPersonsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesNetworkService {

    @GET("movie/popular")
    suspend fun getPopularMovies(@Query("page") pageNumber: Int): Response<PopularPersonsResponse>

}