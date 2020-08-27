package com.tatyanashkolnik.studentassistantkotlin.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.auth.RegistrationActivity

class ProfileFragment : Fragment() {

    private lateinit var rootView: View
    private lateinit var btnLogout: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        initFields()
        initListeners()

        return rootView
    }

    private fun initFields(){
        btnLogout = rootView.findViewById(R.id.logout)
    }

    private fun initListeners(){
        btnLogout.setOnClickListener(View.OnClickListener {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(activity, "You are logged out.", Toast.LENGTH_SHORT).show()
            Log.d("CHECKER","TaskActivity: User logged out successfully.")
            startActivity(Intent(activity, RegistrationActivity::class.java))
        })
    }
}
