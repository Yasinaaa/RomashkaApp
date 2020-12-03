package ru.android.romashkaapp.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar


/**
 * Created by yasina on 19.08.2020.
 * Copyright (c) 2018 Infomatica. All rights reserved.
 */
class Utils {

    companion object {

        val CLIENT_ID = "application"
        val CLIENT_SECRET =  "DNsP4FNATrLFb6WTzL"
        val GRANT_TYPE = "client_credentials"
        val GRANT_TYPE_PASSWORD = "password"
        val ACCESS_TOKEN = "ACCESS_TOKEN"
        val FIRST_OPEN = "FIRST_OPEN"

        val CLIENT_ID_USER = "user"
        val CLIENT_SECRET_USER =  "YOl_BXKysP9QhGmzea"
        
        fun hideKeyboardFrom(context: Context, view: View) {
            val imm: InputMethodManager =
                context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }

        fun showSnackBar(context: Context, view: View, message: String){
            var snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
            snackbar.show()
        }

        fun isFirstOpen(context: Context): Boolean{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getBoolean(FIRST_OPEN, true)
        }

        @SuppressLint("CommitPrefEdits")
        fun makeFirstOpenFalse(context: Context){
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.putBoolean(FIRST_OPEN, false)
            editor.apply()
        }

        fun saveAccessToken(context: Context, accessToken: String?){
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val editor = preferences.edit()
            editor.putString(ACCESS_TOKEN, accessToken)
            editor.apply()
        }

        fun getAccessToken(context: Context): String?{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getString(ACCESS_TOKEN, null)
        }
    }
}
