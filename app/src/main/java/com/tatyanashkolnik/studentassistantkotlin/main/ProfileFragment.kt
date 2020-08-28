package com.tatyanashkolnik.studentassistantkotlin.main

import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.auth.RegistrationActivity
import com.tatyanashkolnik.studentassistantkotlin.data.User
import de.hdodenhof.circleimageview.CircleImageView

class ProfileFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var btnLogout: Button
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var userPhoto: CircleImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        initFields()
        initListeners()

        return rootView
    }

    private fun initFields(){
        etName = rootView.findViewById(R.id.etName)
        etEmail = rootView.findViewById(R.id.etEmail)
        etPassword = rootView.findViewById(R.id.etPassword)
        btnLogout = rootView.findViewById(R.id.logout)
        userPhoto = rootView.findViewById(R.id.userPhoto)

        etEmail.hint = FirebaseAuth.getInstance().currentUser!!.email
        setUsername()
    }

    private fun setUsername(){
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var user = dataSnapshot.getValue<User>(User::class.java)
                etName.hint = user!!.username
                etPassword.hint = user!!.password
//                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, user!!.profileImageUrl)
//                selectUserPhoto.setImageBitmap(bitmap)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(menuListener)
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
