package com.tatyanashkolnik.studentassistantkotlin.auth

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.main.TaskActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.etPassword
import kotlinx.android.synthetic.main.activity_registration.*

class LoginActivity : Activity() {

    private lateinit var btnEnter : TextView
    private lateinit var toRegister : TextView

    private lateinit var email : String
    private lateinit var pwd : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        btnEnter = findViewById(R.id.btnEnter)
        toRegister = findViewById(R.id.toRegister)

        btnEnter.setOnClickListener {
            signInUser()
        }

        toRegister.setOnClickListener {
            finish()
        }
    }

    private fun signInUser(){
        email = etEmail.text.toString()
        pwd = etPassword.text.toString()
        if (email.isEmpty()){
            etEmail.error = "Please enter email."
            etEmail.requestFocus()
        } else if (pwd.isEmpty()) {
            etPassword.error = "Please enter password."
            etPassword.requestFocus()
        } else if (pwd.length < 6) {
            etPassword.error = "Password must be at least 6 characters."
            etPassword.requestFocus()
        } else if (email.isEmpty() && pwd.isEmpty()){
            Toast.makeText(this@LoginActivity, "Fields are empty!", Toast.LENGTH_SHORT).show()
        } else if (!(email.isEmpty() && pwd.isEmpty())) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pwd)
                .addOnFailureListener {
                    Log.d("CHECKER", "Failed to create user: ${it.message}")
                    Toast.makeText(this@LoginActivity, "Login error, try again. ${it.message}", Toast.LENGTH_SHORT).show()
                }
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Toast.makeText(this@LoginActivity, "Error occurred!", Toast.LENGTH_SHORT).show()
                        return@addOnCompleteListener
                    }
                    //else if successful
                    Toast.makeText(this@LoginActivity, "You are logged in.", Toast.LENGTH_SHORT).show()
                    Log.d(
                        "CHECKER",
                        "Successfully created user with uid: ${it.result?.user?.uid}"
                    )
                    startActivity(Intent(this@LoginActivity, TaskActivity::class.java))
                }
        }
    }
}
