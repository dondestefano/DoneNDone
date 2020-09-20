package com.example.donendone

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class PostsActivity : AppCompatActivity() {

    lateinit var postRecyclerView: RecyclerView
    lateinit var addButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts)

        setupRecyclerView()
        setupButton()
    }

    private fun setupRecyclerView() {
        postRecyclerView = findViewById<RecyclerView>(R.id.postRecyclerView)
        postRecyclerView.layoutManager = LinearLayoutManager(this)
        postRecyclerView.adapter = PostRecycleAdapter(this, this)

        PostDataManager.getPosts(this, postRecyclerView.adapter as PostRecycleAdapter)
    }

    private fun setupButton() {
        addButton = findViewById(R.id.addPostActionButton)
        addButton.setOnClickListener {
            val post = PostItem("hjglfd", "Hej", "Nyskapad yo!")
            PostDataManager.uploadPost(this, postRecyclerView.adapter as PostRecycleAdapter, post)
            PostDataManager.getPosts(this, postRecyclerView.adapter as PostRecycleAdapter)
        }
    }
}
