package com.tatyanashkolnik.studentassistantkotlin

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : Activity() {

    private lateinit var btnEnter : TextView
    private lateinit var btnRegister : TextView

    private lateinit var login : String
    private lateinit var pwd : String

    private lateinit var objFirebaseAuth : FirebaseAuth
    private lateinit var objAuthStateListener : FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        objFirebaseAuth = FirebaseAuth.getInstance()
        objAuthStateListener = FirebaseAuth.AuthStateListener {
            var objFirebaseUser = objFirebaseAuth.currentUser
            if(objFirebaseUser != null){
                Toast.makeText(this@LoginActivity, "You are logged in.", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this@LoginActivity, TaskActivity::class.java))
            } else {
                Toast.makeText(this@LoginActivity, "Please log in.", Toast.LENGTH_SHORT).show()
            }
        }


        btnEnter = findViewById(R.id.btnEnter)
        btnRegister = findViewById(R.id.btnRegister)

        btnRegister.setOnClickListener(View.OnClickListener {
            login = etLogin.text.toString()
            pwd = etPassword.text.toString()
            if (login.isEmpty()){
                etLogin.error = "Please enter email."
                etLogin.requestFocus()
            } else if (pwd.isEmpty()){
                etPassword.error = "Please enter password."
                etPassword.requestFocus()
            } else if (login.isEmpty() && pwd.isEmpty()){
                Toast.makeText(this@LoginActivity, "Fields are empty!", Toast.LENGTH_SHORT).show()
            } else if (!(login.isEmpty() && pwd.isEmpty())) {
                objFirebaseAuth.createUserWithEmailAndPassword(login, pwd).addOnCompleteListener(this@LoginActivity, OnCompleteListener {
                    if(!it.isSuccessful){
                        Toast.makeText(this@LoginActivity, "Sign up unsuccessful, please try again.", Toast.LENGTH_SHORT)
                    } else {
                        startActivity(Intent(this@LoginActivity, TaskActivity::class.java))
                    }
                })
            } else {
                Toast.makeText(this@LoginActivity, "Error occurred!", Toast.LENGTH_SHORT).show()
            }
        })

        btnEnter.setOnClickListener(View.OnClickListener {
            login = etLogin.text.toString()
            pwd = etPassword.text.toString()
            if (login.isEmpty()){
                etLogin.error = "Please enter email."
                etLogin.requestFocus()
            } else if (pwd.isEmpty()){
                etPassword.error = "Please enter password."
                etPassword.requestFocus()
            } else if (login.isEmpty() && pwd.isEmpty()){
                Toast.makeText(this@LoginActivity, "Fields are empty!", Toast.LENGTH_SHORT).show()
            } else if (!(login.isEmpty() && pwd.isEmpty())) {
                objFirebaseAuth.signInWithEmailAndPassword(login, pwd).addOnCompleteListener(this@LoginActivity, OnCompleteListener {
                    if(!it.isSuccessful()){
                        Toast.makeText(this@LoginActivity, "Login error, try again.", Toast.LENGTH_SHORT).show()
                    } else {
                        startActivity(Intent(this@LoginActivity, TaskActivity::class.java))
                    }
                })
            } else {
                Toast.makeText(this@LoginActivity, "Error occurred!", Toast.LENGTH_SHORT).show()
            }
        })


    }

    override fun onStart() {
        super.onStart()
        objFirebaseAuth.addAuthStateListener { objAuthStateListener}
    }
}
