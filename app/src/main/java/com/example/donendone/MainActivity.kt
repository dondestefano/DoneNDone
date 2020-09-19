package com.example.donendone

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.liveData
import retrofit2.Response
//TODO: Add TRY and CATCH to GET and DELETE

class MainActivity : AppCompatActivity() {
    private lateinit var text_view: TextView
    private lateinit var button: Button
    private lateinit var deleteButton : Button
    private lateinit var saveButton: Button
    private lateinit var updateButton: Button
    private lateinit var edit_text: EditText
    private lateinit var userService: UserService
    private lateinit var postService: PostService


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getPosts()

        text_view = findViewById(R.id.goal)
        button = findViewById(R.id.button)
        edit_text = findViewById(R.id.text)
        deleteButton = findViewById(R.id.delete_button)
        saveButton = findViewById(R.id.save_button)
        updateButton = findViewById(R.id.update_button)


        userService = RetrofitInstance
                .getRetrofitInstance()
            .create(UserService::class.java)

        postService = RetrofitInstance
            .getRetrofitInstance()
            .create(PostService::class.java)


        button.setOnClickListener() {
            if (edit_text.text != null) {
                getPost(edit_text.text.toString())
            }
        }

        saveButton.setOnClickListener() {
            if (edit_text.text != null) {
                uploadPost(edit_text.text.toString())
            }
        }

        deleteButton.setOnClickListener() {
            if (edit_text.text != null) {
                deletePost(edit_text.text.toString())
            }
        }

        updateButton.setOnClickListener() {
            if (edit_text.text != null) {
                updatePost(edit_text.text.toString())
            }
        }
    }

    fun getPost(id: String) {
        text_view.text =""
        val pathResponse : LiveData<Response<PostItem>> = liveData {
            val response = postService.getOnePost(id)
            emit(response)
        }

        // Get the name of the user when the request has been sent.
        pathResponse.observe(this, Observer{
            val post = it.body()
            val result = " "+"Title: ${post?.title}"+"\n"+
                    " "+"To do: ${post?.content}"
            text_view.append(result)
        })
    }

    private fun getPosts() {
        val responseLiveData: LiveData<Response<Posts>> = liveData {
            val response = postService.getPost()
            emit(response)
        }

        // Get the data of all users when the request has been sent.
        responseLiveData.observe(this, Observer {
            val posts = it.body()?.listIterator()
            if (posts!=null) {
                text_view.text =""
                while (posts.hasNext()){
                    val post = posts.next()

                    val result = " "+"Title: ${post.title}"+"\n"+
                            " "+"To do: ${post.content}"+"\n"+
                            "Id: ${post.id}"+"\n\n"
                    text_view.append(result)
                }
            }
        })
    }

    private fun uploadPost(content: String) {
        val post = PostItem(null, "A post", content)
        val postResponse: LiveData<Response<PostItem>> = liveData {
            val response = postService.uploadPost(post)
            emit(response)
        }
        postResponse.observe(this, Observer {
            val receivedPostItem = it.body()
            val result = " "+"Title: ${receivedPostItem?.title}"+"\n"+
                    " "+"To do: ${receivedPostItem?.content}"
            text_view.text = result

            val id = receivedPostItem?.id
            edit_text.setText(id)
        })

    }

    private fun updatePost (id: String) {
        val post = PostItem("0", "Updated", "3", true)
        val postResponse: LiveData<Response<PostItem>> = liveData {
            val response = postService.updatePost(id, post)
            emit(response)
        }
        postResponse.observe(this, Observer {
            val receivedPostItem = it.body()
            val result = " "+"Title: ${receivedPostItem?.title}"+"\n"+
                    " "+"To do: ${receivedPostItem?.content}"
            text_view.text = result
        })
    }

    private fun deletePost(id: String) {
        val pathResponse : LiveData<Response<Void>> = liveData {
            val response = postService.deletePost(id)
            emit(response)
        }

        pathResponse.observe(this, Observer{
            text_view.text = "deleted"
        })
    }

/*    private fun getUsers() {
        val responseLiveData: LiveData<Response<Users>> = liveData {
            val response = retService.getUser()
            emit(response)
        }

        // Get the data of all users when the request has been sent.
        responseLiveData.observe(this, Observer {
            val users = it.body()?.listIterator()
            if (users!=null) {
                text_view.text =""
                while (users.hasNext()){
                    val user = users.next()

                    val result = " "+"Name: ${user.name}"+"\n"+
                            " "+"Last name: ${user.lastName}"
                    text_view.append(result)
                }
            }
        })
    }

    private fun uploadUser(name: String) {
        val user = UserItem("0", name, "3")
        val postResponse: LiveData<Response<UserItem>> = liveData {
            val response = retService.uploadUser(user)
            emit(response)
        }
        postResponse.observe(this, Observer {
            val receivedUserItem = it.body()
            val result = " " + "Id : ${receivedUserItem?.id}" + "\n" +
                    " " + "Name : ${receivedUserItem?.name}" + "\n" + "\n\n\n"
            text_view.text = result
        })

    }

    private fun updateUser(id: String) {
        val user = UserItem("0", "Updated", "3")
        val postResponse: LiveData<Response<UserItem>> = liveData {
            val response = retService.updateUser(id, user)
            emit(response)
        }
        postResponse.observe(this, Observer {
            val receivedUserItem = it.body()
            val result = " " + "Id : ${receivedUserItem?.id}" + "\n" +
                    " " + "New name : ${receivedUserItem?.name}" + "\n" + "\n\n\n"
            text_view.text = result
        })
    }

    private fun deleteUser(id: String) {
        val pathResponse : LiveData<Response<Void>> = liveData {
            val response = retService.deleteUser(id)
            emit(response)
        }

        pathResponse.observe(this, Observer{
            text_view.text = "deleted"
        })
    }*/
}