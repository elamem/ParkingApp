package com.elamparithi.parkypark.data.remote

import com.elamparithi.parkypark.data.remote.response.DirectionResponse
import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkService {


    @GET(Endpoints.DIRECTIONS)
    fun getDirection(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("key") key: String = Networking.API_KEY
    ) : Single<DirectionResponse>

}