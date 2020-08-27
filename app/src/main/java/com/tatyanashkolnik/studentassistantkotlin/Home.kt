package com.tatyanashkolnik.studentassistantkotlin

import android.app.Application
import android.content.Intent
import com.google.firebase.auth.FirebaseAuth
import com.tatyanashkolnik.studentassistantkotlin.main.TaskActivity

class Home : Application() {

    override fun onCreate() {
        super.onCreate()
        if(FirebaseAuth.getInstance().currentUser != null) {
            var intent = Intent((applicationContext), TaskActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}