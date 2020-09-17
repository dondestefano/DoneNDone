package com.example.donendone

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @GET("user")
    suspend fun getUser(): Response<Users>

    @GET("user/{id}")
    suspend fun getOneUser(@Path(value = "id") userId: String): Response<UserItem>

    @POST("user")
    suspend fun uploadUser(@Body user: UserItem): Response<UserItem>

    @PUT("user/{id}")
    suspend fun updateUser(@Path(value = "id")userId: String, @Body user: UserItem): Response<UserItem>

    @DELETE("user/{id}")
    suspend fun deleteUser(@Path(value = "id") userId: String): Response<Void>
}