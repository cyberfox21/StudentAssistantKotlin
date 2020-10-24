package com.tatyanashkolnik.studentassistantkotlin.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.tatyanashkolnik.studentassistantkotlin.Constants
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.main.TaskActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : Activity() {

    private lateinit var btnEnter: TextView
    private lateinit var toRegister: TextView

    private lateinit var email: String
    private lateinit var pwd: String
    private lateinit var sharedPrefs  : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)

        sharedPrefs = this.getSharedPreferences(Constants.KEY_THEME, Context.MODE_PRIVATE)


        when(this.getSharedPreferences(Constants.KEY_THEME, Context.MODE_PRIVATE)!!.getInt(Constants.SWITCHER_STATE, 0)){
            1 -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, Constants.THEME_DARK)
            0 -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, Constants.THEME_LIGHT)
        }

        btnEnter = findViewById(R.id.btnEnter)
        toRegister = findViewById(R.id.toRegister)

        btnEnter.setOnClickListener {
            signInUser()
        }

        toRegister.setOnClickListener {
            finish()
        }
    }

    private fun signInUser() {
        email = etLoginEmail.text.toString()
        pwd = etLoginPassword.text.toString()
        if (email.isEmpty()) {
            Log.d("CHECKER", "LoginActivity: Please enter email.")
            etLoginEmail.error = "Please enter email."
            etLoginEmail.requestFocus()
        } else if (pwd.isEmpty()) {
            Log.d("CHECKER", "LoginActivity: Please enter password.")
            etLoginPassword.error = "Please enter password."
            etLoginPassword.requestFocus()
        } else if (pwd.length < 6) {
            Log.d("CHECKER", "LoginActivity: Password must be at least 6 characters.")
            etLoginPassword.error = "Password must be at least 6 characters."
            etLoginPassword.requestFocus()
        } else if (email.isEmpty() && pwd.isEmpty()) {
            Log.d("CHECKER", "LoginActivity: Fields are empty!")
            Toast.makeText(this@LoginActivity, "Fields are empty!", Toast.LENGTH_SHORT).show()
        } else if (!(email.isEmpty() && pwd.isEmpty())) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pwd)
                .addOnFailureListener {
                    Log.d("CHECKER", "LoginActivity: Failed: ${it.message}")
                    Toast.makeText(this@LoginActivity, "Failed: ${it.message}", Toast.LENGTH_LONG).show()
                }
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Log.d("CHECKER", "LoginActivity: Error occurred!")
                        Toast.makeText(this@LoginActivity, "Error occurred!", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                    // else if successful
                    // Toast.makeText(this@LoginActivity, "You are logged in.", Toast.LENGTH_SHORT).show()
                    Log.d(
                        "CHECKER",
                        "LoginActivity: User logged in successfully with uid: ${it.result?.user?.uid}"
                    )
                    startActivity(Intent(this@LoginActivity, TaskActivity::class.java))
                }
        }
    }
    private fun saveTheme(theme: Int) = sharedPrefs.edit().putInt(Constants.SWITCHER_STATE, theme).apply()

    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveTheme(prefsMode)
    }
}
