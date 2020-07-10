package com.esmaeel.moviesapp.data.remote

import com.esmaeel.moviesapp.data.models.PersonsImagesResponse
import com.esmaeel.moviesapp.data.models.PopularPersonsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesNetworkService {

    @GET("person/popular")
    suspend fun getPopularPersons(@Query("page") pageNumber: Int): Response<PopularPersonsResponse>

    @GET("person/{person_id}/images")
    suspend fun getPersonImages(@Path("person_id") personId: Int?): Response<PersonsImagesResponse>

}