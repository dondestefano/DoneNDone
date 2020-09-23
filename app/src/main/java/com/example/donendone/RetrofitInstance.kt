package com.example.donendone

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
    companion object {
        const val FIREBASE_URL = "https://us-central1-doneapi.cloudfunctions.net/"
        const val LOCALHOST_URL = "https://localhost:5001/doneapi/us-central1/post/"

        private const val BASE_URL = FIREBASE_URL
        fun getRetrofitInstance(): Retrofit {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }
    }
}