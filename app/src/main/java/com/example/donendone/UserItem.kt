package com.example.donendone

import com.google.gson.annotations.SerializedName

data class UserItem(
    @SerializedName("id")
    val id: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("lastName")
    val lastName: String
)