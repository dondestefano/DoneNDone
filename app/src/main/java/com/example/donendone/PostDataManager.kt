package com.example.donendone

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import retrofit2.Response

object PostDataManager {
    val posts = mutableListOf<PostItem>()
    val postService = RetrofitInstance
        .getRetrofitInstance()
        .create(PostService::class.java)

    fun getPosts(lifecycleOwner: LifecycleOwner, postRecycleAdapter: PostRecycleAdapter) {
        // Create LiveData asking for a Posts response and start the API request.
        val responseLiveData: LiveData<Response<Posts>> = liveData {
            val response = postService.getPost()
            emit(response)
        }

        // Wait for the response and add the received Posts to PostDataManager.
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

    fun getPost(lifecycleOwner: LifecycleOwner, id: String): PostItem? {
        var post = PostItem("not found", "Error", "Could not find post", false)
        val pathResponse : LiveData<Response<PostItem>> = liveData {
            val response = postService.getOnePost(id)
            emit(response)
        }

        // Get the name of the user when the request has been sent.
        pathResponse.observe(lifecycleOwner, Observer{
            post = it.body()!!
        })
        return post
    }

    fun uploadPost(lifecycleOwner: LifecycleOwner, postRecycleAdapter: PostRecycleAdapter, post: PostItem) {
        // Create LiveData asking for a PostItem response and start the API request.
        val postResponse: LiveData<Response<PostItem>> = liveData {
            val response = postService.uploadPost(post)
            emit(response)
        }
        postResponse.observe(lifecycleOwner, Observer {
            val receivedPostItem = it.body()
            receivedPostItem?.let {  posts.add(receivedPostItem) }
            posts.sortBy { receivedPostItem!!.status }

            postRecycleAdapter.updateItemsToList()
        })
    }

    fun updatePost (lifecycleOwner: LifecycleOwner, postRecycleAdapter: PostRecycleAdapter, post: PostItem) {
        val postResponse: LiveData<Response<PostItem>> = liveData {
            val response = postService.updatePost(post.id, post)
            emit(response)
        }
        postResponse.observe(lifecycleOwner, Observer {
            val receivedPostItem = it.body()
            if (receivedPostItem != null) {
                var pos = 0
                for (postItem in posts) {
                    if (postItem.id == receivedPostItem.id) {
                        posts[pos] = receivedPostItem

                    } else {
                        pos++
                    }
                }
            }
            postRecycleAdapter.updateItemsToList()
        })
    }

    fun deletePost(lifecycleOwner: LifecycleOwner, position: Int, postRecycleAdapter: PostRecycleAdapter) {
        val pathResponse : LiveData<Response<Void>> = liveData {
            val post = posts[position]
            val response = postService.deletePost(post.id)
            emit(response)
        }

        pathResponse.observe(lifecycleOwner, Observer{
        })

        posts.removeAt(position)
    }
}