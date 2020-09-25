package com.example.donendone

import retrofit2.Response
import retrofit2.http.*

//Interface to be used with RetrofitInstance.

interface PostService {
    @GET("post")
    suspend fun getPost(): Response<Posts>

    @GET("post/{id}")
    suspend fun getOnePost(@Path(value = "id") userId: String): Response<PostItem>

    @POST("post")
    suspend fun uploadPost(@Body post: PostItem): Response<PostItem>

    @PUT("post/{id}")
    suspend fun updatePost(@Path(value = "id")userId: String, @Body post: PostItem): Response<PostItem>

    @DELETE("post/{id}")
    suspend fun deletePost(@Path(value = "id") userId: String): Response<Void>
}