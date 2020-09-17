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


class MainActivity : AppCompatActivity() {
    private lateinit var text_view: TextView
    private lateinit var button: Button
    private lateinit var edit_text: EditText
    private lateinit var retService: UserService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getUsers()

        text_view = findViewById(R.id.goal)
        button = findViewById(R.id.button)
        edit_text = findViewById(R.id.text)
        retService = RetrofitInstance
            .getRetrofitInstance()
            .create(UserService::class.java)

        uploadUser()

        button.setOnClickListener() {
            if (edit_text.text != null) {
                getUser(edit_text.text.toString())
            }
        }
    }

    fun getUser(id: String) {
        text_view.text =""
        val pathResponse : LiveData<Response<UserItem>> = liveData {
            val response = retService.getOneUser(id)
            emit(response)
        }

        pathResponse.observe(this, Observer{
            val name = it.body()?.name
            text_view.append(name)
        })
    }

    fun getUsers() {
        val responseLiveData: LiveData<Response<Users>> = liveData {
            val response = retService.getUser()
            emit(response)
        }

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

    private fun uploadUser() {
        val user = UserItem("15", "name", "lastname")
        val postResponse: LiveData<Response<UserItem>> = liveData {
            val response = retService.uploadUser(user)
            emit(response)
        }
        postResponse.observe(this, Observer {
            val receivedUserItem = it.body()
            text_view.append(receivedUserItem.toString())
        })
    }
}