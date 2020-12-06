package com.tatyanashkolnik.studentassistantkotlin.addcards

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.data.PasswordCard

class AddCardActivity() : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var etService: EditText
    private lateinit var etLogin: EditText
    private lateinit var etPassword: EditText

    private lateinit var model: PasswordCard

    private var cardRef = FirebaseDatabase.getInstance().reference.child("passwords").child(FirebaseAuth.getInstance().uid.toString())

    private lateinit var key: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        key = intent.getStringExtra("key")
        initFields()
        initListeners()
    }

    private fun initFields() {
        fab = findViewById(R.id.fabSendPassword)
        etService = findViewById(R.id.etService)
        etLogin = findViewById(R.id.etLogin)
        etPassword = findViewById(R.id.etPassword)
        if (key == "edit") {
            etService.setText(intent.getStringExtra("service"))
            etLogin.setText(intent.getStringExtra("login"))
            etPassword.setText(intent.getStringExtra("password"))
        }
    }

    private fun initListeners() {
        fab.setOnClickListener {
            when (key) {
                "edit" -> changePasswordCard()
                "add" -> createPasswordCard()
            }
        }
    }

    private fun changePasswordCard() {
        var path = intent.getStringExtra("path")

        model = PasswordCard(
            etService.text.toString() ?: "",
            etLogin.text.toString() ?: "",
            etPassword.text.toString() ?: "",
            path
        )

        Log.d(
            "CHECKER",
            "PasswordCard Added" +
                "Service: ${model.service} | Login: ${model.login} | Password: ${model.password} | Path: ${model.path}"
        )

        cardRef.child(path).setValue(
            model
        )

        finish()
    }

    private fun createPasswordCard() {
        var path = cardRef.push().key.toString()
        Log.d("T", "AddCardActivity card path: $path")

        model = PasswordCard(
            etService.text.toString() ?: "",
            etLogin.text.toString() ?: "",
            etPassword.text.toString() ?: "",
            path ?: ""
        )

        Log.d(
            "CHECKER",
            "PasswordCard Added" +
                "Service: ${model.service} | Login: ${model.login} | Password: ${model.password} | Path: ${model.path}"
        )

        cardRef.child(path).setValue(
            model
        )

        finish()
    }
}
