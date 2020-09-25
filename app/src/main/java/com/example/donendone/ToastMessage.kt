package com.example.donendone

import android.content.Context
import android.widget.Toast

//Class used to display errors in the app.

class ToastMessage (private val message: String, private val context: Context) {

    fun errorToast() {
        Toast.makeText(this.context, this.message, Toast.LENGTH_SHORT)
            .show()
    }
}