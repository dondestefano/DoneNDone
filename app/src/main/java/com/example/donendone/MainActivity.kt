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
    private lateinit var retService: UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getUsers()

        text_view = findViewById(R.id.goal)
        button = findViewById(R.id.button)
        edit_text = findViewById(R.id.text)
        deleteButton = findViewById(R.id.delete_button)
        saveButton = findViewById(R.id.save_button)
        updateButton = findViewById(R.id.update_button)


        retService = RetrofitInstance
            .getRetrofitInstance()
            .create(UserService::class.java)


        button.setOnClickListener() {
            if (edit_text.text != null) {
                getUser(edit_text.text.toString())
            }
        }

        saveButton.setOnClickListener() {
            if (edit_text.text != null) {
                uploadUser(edit_text.text.toString())
            }
        }

        deleteButton.setOnClickListener() {
            if (edit_text.text != null) {
                deleteUser(edit_text.text.toString())
            }
        }

        updateButton.setOnClickListener() {
            if (edit_text.text != null) {
                updateUser(edit_text.text.toString())
            }
        }
    }

    fun getUser(id: String) {
        text_view.text =""
        val pathResponse : LiveData<Response<UserItem>> = liveData {
            val response = retService.getOneUser(id)
            emit(response)
        }

        // Get the name of the user when the request has been sent.
        pathResponse.observe(this, Observer{
            val name = it.body()?.name
            text_view.append(name)
        })
    }

    private fun getUsers() {
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
    }
}