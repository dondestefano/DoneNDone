package com.example.donendone

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Response

object PostDataManager {
    val posts = mutableListOf<PostItem>()
    val postService = RetrofitInstance
        .getRetrofitInstance()
        .create(PostService::class.java)

    fun getPosts(lifecycleOwner: LifecycleOwner, postRecycleAdapter: PostRecycleAdapter) {
        val responseLiveData: LiveData<Response<Posts>> = liveData {
            val response = postService.getPost()
            emit(response)
        }

        // Get the data of all users when the request has been sent.
        responseLiveData.observe(lifecycleOwner, Observer {
            val receivedPosts = it.body()?.listIterator()
            if (receivedPosts!=null) {
                while (receivedPosts.hasNext()){
                    val post = receivedPosts.next()
                    posts.add(post)
                }

                postRecycleAdapter.updateItemsToList()
            }
        })
    }
}