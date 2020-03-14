package ourcorner.pushtest

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity


fun AppCompatActivity.hideKeyboard() {
    this.currentFocus?.let {
        val inputManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}