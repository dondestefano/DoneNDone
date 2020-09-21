package com.example.donendone

import android.app.AlertDialog
import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.new_post_dialog.view.*

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
            showNewPostDialog()
        }
    }

    private fun showNewPostDialog() {
        val customDialogView = LayoutInflater.from(this).inflate(R.layout.new_post_dialog, null)
        //AlertDialogBuilder.
        val mBuilder = AlertDialog.Builder(this)
            .setView(customDialogView)
            .setTitle("Add new To-do")
        //show dialog.
        val  mAlertDialog = mBuilder.show()
        //login button click of custom layout.
        customDialogView.dialogAddButton.setOnClickListener {
            //get text from EditTexts.
            val postTitle = customDialogView.dialogTitleEditText.text.toString()
            val postContent = customDialogView.dialogContentEditText.text.toString()

            // Create new post with title, content and temporary ID.
            val newPost = PostItem("id", postTitle, postContent, false)

            // Upload post with PostDataManager
            PostDataManager.uploadPost(this, postRecyclerView.adapter as PostRecycleAdapter, newPost)

            //dismiss dialog.
            mAlertDialog.dismiss()
        }
            //cancel button click of custom layout
            customDialogView.dialogCancelButton.setOnClickListener {
                // Dismiss the dialog if the user cancels.
                mAlertDialog.dismiss()
        }
    }
}
