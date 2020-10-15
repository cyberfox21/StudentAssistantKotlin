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

class AddCardActivity : AppCompatActivity() {

    private lateinit var fab: FloatingActionButton
    private lateinit var etService: EditText
    private lateinit var etLogin: EditText
    private lateinit var etPassword: EditText

    private lateinit var model: PasswordCard

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_card)
        initFields()
        initListeners()
    }

    private fun initFields() {
        fab = findViewById(R.id.fabSendPassword)
        etService = findViewById(R.id.tvService)
        etLogin = findViewById(R.id.tvLogin)
        etPassword = findViewById(R.id.tvPassword)
    }

    private fun initListeners() {
        fab.setOnClickListener {
            updatePasswordCard()
        }
    }

    private fun updatePasswordCard() {
        model = PasswordCard(
            etService.text.toString() ?: "",
            etLogin.text.toString() ?: "",
            etPassword.text.toString() ?: ""
        )

        Log.d("CHECKER", "Service: $etService.text | Login: $etLogin.text | Password: $etPassword.text")

        FirebaseDatabase.getInstance().reference.child("passwords").child(FirebaseAuth.getInstance().uid.toString()).push()
            .setValue(
                model
            )

        finish()
    }
}
