package com.example.donendone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PostsActivity : AppCompatActivity() {

    lateinit var postRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        postRecyclerView = findViewById<RecyclerView>(R.id.postRecyclerView)
        postRecyclerView.layoutManager = LinearLayoutManager(this)
        postRecyclerView.adapter = PostRecycleAdapter(this)

        PostDataManager.getPosts(this, postRecyclerView.adapter as PostRecycleAdapter)

    }
}
