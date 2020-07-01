package com.tatyanashkolnik.studentassistantkotlin.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.auth.RegistrationActivity

class TaskActivity : Activity() {

    private lateinit var btnLogout : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        btnLogout = findViewById(R.id.logout)

        btnLogout.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(this@TaskActivity, "You are logged out.", Toast.LENGTH_SHORT).show()
            Log.d("CHECKER","TaskActivity: User logged out successfully.")
            startActivity(Intent(this@TaskActivity, RegistrationActivity::class.java))
        })
    }
}
