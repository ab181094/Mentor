package com.psygon.tech.mentor.helpers

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast

fun toast(context: Context, any: Any?) {
    Toast.makeText(context, any.toString(), Toast.LENGTH_LONG).show()
}

fun log(any: Any?) {
    Log.d("MENTOR_TEST", any.toString())
}

fun showProgressBar(progressBar: ProgressBar) {
    progressBar.visibility = View.VISIBLE
}

fun hideProgressBar(progressBar: ProgressBar) {
    progressBar.visibility = View.INVISIBLE
}