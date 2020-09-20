package com.example.donendone

import com.google.gson.annotations.SerializedName

data class PostItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("title")
    var title: String,
    @SerializedName("content")
    var content: String,
    @SerializedName("status")
    var status: Boolean = false
)
