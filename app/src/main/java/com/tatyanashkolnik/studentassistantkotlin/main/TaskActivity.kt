package com.tatyanashkolnik.studentassistantkotlin.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.auth.RegistrationActivity

class TaskActivity : Activity() {

    private lateinit var btnLogout : Button
    private lateinit var objAuthStateListener : FirebaseAuth.AuthStateListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        btnLogout = findViewById(R.id.logout)

        btnLogout.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@TaskActivity, RegistrationActivity::class.java))
        })
    }
}
