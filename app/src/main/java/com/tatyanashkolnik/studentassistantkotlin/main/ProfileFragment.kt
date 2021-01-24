package com.tatyanashkolnik.studentassistantkotlin.main

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.auth.RegistrationActivity
import com.tatyanashkolnik.studentassistantkotlin.data.User
import de.hdodenhof.circleimageview.CircleImageView
import java.net.URL
import java.util.UUID

class ProfileFragment : Fragment() {

    private lateinit var rootView: View

    private lateinit var btnLogout: Button
    private lateinit var btnChange: TextView
    private lateinit var etName: EditText
    private lateinit var etEmail: TextView
    private lateinit var etPassword: EditText
    private lateinit var userPhoto: CircleImageView

    private var selectedPhotoUri: Uri? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(R.layout.fragment_profile, container, false)

        initFields()
        initListeners()

        return rootView
    }

    private fun initFields() {
        etName = rootView.findViewById(R.id.etName)
        etEmail = rootView.findViewById(R.id.etEmail)
        etPassword = rootView.findViewById(R.id.etRegistrationPassword)
        btnLogout = rootView.findViewById(R.id.logout)
        btnChange = rootView.findViewById(R.id.btnChange)
        userPhoto = rootView.findViewById(R.id.userProfilePhoto)

        etEmail.hint = FirebaseAuth.getInstance().currentUser!!.email
        setUsername()
    }

    private fun setUsername() {
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                var user = dataSnapshot.getValue<User>(User::class.java)
                etName.setText(user!!.username)
                etPassword.setText(user!!.password)
                Picasso.get().load(user.profileImageUrl).into(userPhoto)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        FirebaseDatabase.getInstance().reference.child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
            .addListenerForSingleValueEvent(menuListener)
    }

    private fun initListeners() {
        userPhoto.setOnClickListener {
            changePhoto()
        }
        btnLogout.setOnClickListener {
            logout()
        }
        btnChange.setOnClickListener {
            sendChanges()
        }
    }

    private fun changePhoto() {
        Log.d("CHECKER", "ProfileFragment: Try to show photo galary")
        val toGalary = Intent(Intent.ACTION_PICK)
        toGalary.type = "image/*"
        startActivityForResult(toGalary, 0)
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(activity, "You are logged out.", Toast.LENGTH_SHORT).show()
        Log.d("CHECKER", "TaskActivity: User logged out successfully.")
        startActivity(Intent(activity, RegistrationActivity::class.java))
    }

    private fun sendChanges() {

        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                selectedPhotoUri = Uri.parse(user?.profileImageUrl)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/avatars/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener { it ->
                Log.d(
                    "CHECKER",
                    "RegistrationActivity: Successfully uploaded image: ${it.metadata?.path}"
                )

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("CHECKER", "RegistrationActivity: File location: $it")
                    selectedPhotoUri = it
                    saveUserToDatabase()
                }
            }
            .addOnFailureListener {
                Log.d("CHECKER", "RegistrationActivity: Failed to upload image.")
            }

        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseDatabase.getInstance().reference.child("users").child(
                FirebaseAuth.getInstance()
                    .currentUser!!.uid
            ).addListenerForSingleValueEvent(menuListener)
        }

        saveUserToDatabase()
    }

    private fun saveUserToDatabase() {
        val model = User(
            (FirebaseAuth.getInstance().currentUser?.uid ?: ""),
            etName.text.toString(),
            etPassword.text.toString(),
            selectedPhotoUri.toString()
        )

        FirebaseAuth.getInstance().currentUser?.let {
            FirebaseDatabase.getInstance().reference.child("users").child(it.uid).setValue(
                model
            )
        }?.addOnSuccessListener {
            Toast.makeText(activity, "Profil was changed successfully!", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("CHECKER", "RegistrationActivity: Photo was selected")

            selectedPhotoUri = data.data

            // deletePreviosUserPhotoFromStorage()

            val bitmap = MediaStore.Images.Media.getBitmap(requireActivity().contentResolver, selectedPhotoUri)
            userPhoto.setImageBitmap(bitmap)
        }
    }

    private fun deletePreviosUserPhotoFromStorage() {

        var photoRef: StorageReference

        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                val prevPhoto = Uri.parse(user?.profileImageUrl)
                photoRef = FirebaseStorage.getInstance().getReferenceFromUrl(
                    URL(prevPhoto.toString()).toString()
                )
                if (photoRef != null) {
                    photoRef.delete().addOnSuccessListener { // File deleted successfully
                        Log.d("CHECKER", "onSuccess: deleted file")
                    }.addOnFailureListener { // Uh-oh, an error occurred!
                        Log.d("CHECKER", "onFailure: did not delete file")
                    }
                }
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }

        if (FirebaseAuth.getInstance().currentUser != null) {
            FirebaseDatabase.getInstance().reference.child("users").child(
                FirebaseAuth.getInstance()
                    .currentUser!!.uid
            ).addListenerForSingleValueEvent(menuListener)
        }
    }
}
