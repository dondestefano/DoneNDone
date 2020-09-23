package com.example.donendone

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.graphics.Paint
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.new_post_dialog.view.*
import kotlinx.coroutines.delay
import org.w3c.dom.Text

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

        when (posts[position].status) {
            false -> {
                holder.statusCheckBox.isChecked = false
                holder.postTitleText.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }

            true -> {
                holder.statusCheckBox.isChecked = true
                holder.postTitleText.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            }
        }

    }

    fun updateItemsToList() {
        posts = PostDataManager.posts
        posts.sortBy { it.status }
        notifyDataSetChanged()
    }

    inner class ViewHolder(postView: View) : RecyclerView.ViewHolder(postView) {
        val postTitleText: TextView = itemView.findViewById<TextView>(R.id.textViewTitle)
        val postContentText: TextView = itemView.findViewById<TextView>(R.id.textViewContent)
        val deleteButton: ImageView = itemView.findViewById<ImageView>(R.id.deleteButton)
        val updateButton: ImageView = itemView.findViewById<ImageView>(R.id.updateButton)
        val statusCheckBox: CheckBox = itemView.findViewById<CheckBox>(R.id.statusCheckBox)
        lateinit var recycleAdapter: PostRecycleAdapter
        var pos = 0

        private fun showUpdatePostDialog() {
            val customDialogView = LayoutInflater.from(context).inflate(R.layout.new_post_dialog, null)
            //AlertDialogBuilder.
            val mBuilder = AlertDialog.Builder(context)
                .setView(customDialogView)

            //Set title and content of the ViewHolders post.
            customDialogView.dialogTitleEditText.setText(postTitleText.text)
            customDialogView.dialogContentEditText.setText(postContentText.text)
            customDialogView.dialogAddButton.text = "Update"
            customDialogView.dialogTitle.text = "Edit To-do"

            val  mAlertDialog = mBuilder.show()
            customDialogView.dialogAddButton.setOnClickListener {

                if (customDialogView.dialogTitleEditText.text.isEmpty()){
                    val toast = ToastMessage("Please enter a title for your To-do.", context)
                    toast.errorToast()
                }
                else {
                    //get text from EditTexts.
                    val postTitle = customDialogView.dialogTitleEditText.text.toString()
                    val postContent = customDialogView.dialogContentEditText.text.toString()

                    // Create new post with title, content and temporary ID.
                    val newPost = PostItem(posts[pos].id, postTitle, postContent, posts[pos].status)

                    // Upload post with PostDataManager
                    PostDataManager.updatePost(lifecycleOwner, recycleAdapter, newPost)

                    //dismiss dialog.
                    mAlertDialog.dismiss()
                }
            }
            //cancel button click of custom layout
            customDialogView.dialogCancelButton.setOnClickListener {
                // Dismiss the dialog if the user cancels.
                mAlertDialog.dismiss()
            }
        }

        init {
            deleteButton.setOnClickListener {
                val dialogBuilder = AlertDialog.Builder(context)
                dialogBuilder.setTitle("Delete to-do?")
                    .setMessage("Remove the To-do from your list. It cannot be restored.")
                    .setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, id ->
                        PostDataManager.deletePost(lifecycleOwner, pos, recycleAdapter)
                        notifyDataSetChanged()
                    })
                    .setNegativeButton("No", DialogInterface.OnClickListener {
                            dialog, id -> dialog.cancel()
                    })
                    .show()
            }

            updateButton.setOnClickListener {
                showUpdatePostDialog()
            }

            statusCheckBox.setOnClickListener{
                var post = posts[pos]
                post.status =! post.status
                PostDataManager.updatePost(lifecycleOwner, recycleAdapter, post)
            }
        }
    }
}