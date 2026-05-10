package com.colombo.choresmanager.network

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ChoresApi {
    @GET("api/chores")
    suspend fun getChores(@Header("Authorization") bearerToken: String): List<RemoteChore>

    @POST("api/chores")
    suspend fun createChore(
        @Header("Authorization") bearerToken: String,
        @Body request: CreateChoreRequest,
    ): RemoteChore

    @POST("api/chores/{id}/complete")
    suspend fun completeChore(
        @Header("Authorization") bearerToken: String,
        @Path("id") choreId: Int,
        @Body request: CompleteChoreRequest,
    ): RemoteChore

    @DELETE("api/chores/{id}")
    suspend fun deleteChore(
        @Header("Authorization") bearerToken: String,
        @Path("id") choreId: Int,
    )
}
