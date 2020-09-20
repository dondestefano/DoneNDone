package com.example.donendone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.delay

class PostRecycleAdapter(private val context: Context, private val lifecycleOwner: LifecycleOwner) : RecyclerView.Adapter<PostRecycleAdapter.ViewHolder>()  {
    private val layoutInflator = LayoutInflater.from(context)
    var posts  = mutableListOf<PostItem>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflator.inflate(R.layout.post_card_layout, parent, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount() = posts.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentPost = posts[position]
        holder.postTitleText.text = currentPost?.title
        holder.postContentText.text = currentPost?.content
        holder.recycleAdapter = this
        holder.pos = position
    }

    fun updateItemsToList() {
        posts = PostDataManager.posts
        notifyDataSetChanged()
    }

    inner class ViewHolder(postView: View) : RecyclerView.ViewHolder(postView) {
        val postTitleText: TextView = itemView.findViewById<TextView>(R.id.textViewTitle)
        val postContentText: TextView = itemView.findViewById<TextView>(R.id.textViewContent)
        val deleteButton: Button = itemView.findViewById<Button>(R.id.deleteButton)
        val updateButton: Button = itemView.findViewById<Button>(R.id.updateButton)
        lateinit var recycleAdapter: PostRecycleAdapter
        var pos = 0

        init {
            deleteButton.setOnClickListener {
                PostDataManager.deletePost(lifecycleOwner, pos, recycleAdapter)
                notifyDataSetChanged()
            }

            updateButton.setOnClickListener {
                PostDataManager.updatePost(lifecycleOwner, recycleAdapter, posts[pos])
            }
        }
    }
}