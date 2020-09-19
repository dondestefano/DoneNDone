package com.example.donendone

import com.google.gson.annotations.SerializedName

data class PostItem(
    @SerializedName("id")
    val id: String?,
    @SerializedName("title")
    val title: String,
    @SerializedName("content")
    val content: String,
    @SerializedName("status")
    val status: Boolean = false
)
