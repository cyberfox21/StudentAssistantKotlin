package com.tatyanashkolnik.studentassistantkotlin.auth

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.data.User
import com.tatyanashkolnik.studentassistantkotlin.main.TaskActivity
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

class RegistrationActivity : Activity() {

    private lateinit var selectUserPhoto : ImageView

    private lateinit var btnRegister : TextView
    private lateinit var toEnter : TextView

    private lateinit var name : String
    private lateinit var email : String
    private lateinit var pwd : String

    private var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        btnRegister = findViewById(R.id.btnRegister)
        selectUserPhoto = findViewById(R.id.selectUserPhoto)
        toEnter = findViewById(R.id.toEnter)

        btnRegister.setOnClickListener{
            registerUser()
        }

        toEnter.setOnClickListener {
            startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
        }

        selectUserPhoto.setOnClickListener {
            Log.d("CHECKER", "Try to show photo galary")
            val toGalary = Intent(Intent.ACTION_PICK)
            toGalary.type = "image/*"
            startActivityForResult(toGalary, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("CHECKER", "Photo was selected")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            selectUserPhoto.setImageBitmap(bitmap)
//            val bitmapDrawble = BitmapDrawable(bitmap)
//            selectUserPhoto.setBackgroundDrawable(bitmapDrawble)
        }
    }

    private fun uploadImageToFirebaseStorage(){
        if(selectedPhotoUri == null) return
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/avatars/$filename")
        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("CHECKER", "RegistrationActivity: Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnCompleteListener{
                    Log.d("CHECKER", "RegistrationActivity: File location: $it")
                    saveUserToDatabase(it.toString())
                }
            }
            .addOnFailureListener{
                Log.d("CHECKER", "RegistrationActivity: Failed to upload image.")
                Toast.makeText(this@RegistrationActivity, "Failed to upload image.", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveUserToDatabase(profileImageUrl : String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, etName.text.toString(), profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("CHECKER", "RegistrationActivity: User saved in Firebase.")
            }
            .addOnFailureListener{
                Log.d("CHECKER", "RegistrationActivity: Failed to save user in Firebase.")
            }
    }

    private fun registerUser(){
        name = etName.text.toString()
        email = etEmail.text.toString()
        pwd = etPassword.text.toString()
        if (email.isEmpty()){
            Log.d("CHECKER", "RegistrationActivity: Please enter email.")
            etEmail.error = "Please enter email."
            etEmail.requestFocus()
        } else if (pwd.isEmpty()) {
            Log.d("CHECKER", "RegistrationActivity: Please enter password.")
            etPassword.error = "Please enter password."
            etPassword.requestFocus()
        } else if (name.isEmpty()) {
            Log.d("CHECKER", "RegistrationActivity: Please enter name.")
            etName.error = "Please enter name."
            etName.requestFocus()
        } else if (pwd.length < 6) {
            Log.d("CHECKER", "RegistrationActivity: Password must be at least 6 characters.")
            etPassword.error = "Password must be at least 6 characters."
            etPassword.requestFocus()
        } else if (email.isEmpty() && pwd.isEmpty()){
            Log.d("CHECKER", "RegistrationActivity: Fields are empty!")
            Toast.makeText(this@RegistrationActivity, "Fields are empty!", Toast.LENGTH_SHORT).show()
        } else if (!(email.isEmpty() && pwd.isEmpty())) {
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, pwd)
                .addOnFailureListener {
                    Log.d("CHECKER", "RegistrationActivity: Failed to create user. ${it.message}")
                    Log.d("CHECKER", "RegistrationActivity: Name: $name")
                    Log.d("CHECKER", "RegistrationActivity: Email: $email")
                    Log.d("CHECKER", "RegistrationActivity: Password: $pwd")
                    Toast.makeText(this@RegistrationActivity, "Failed to create user: ${it.message}", Toast.LENGTH_LONG).show()
                }
                .addOnCompleteListener {
                    if (!it.isSuccessful) {
                        Log.d("CHECKER", "RegistrationActivity: Failed to create user.")
                        Log.d("CHECKER", "RegistrationActivity: Name: $name")
                        Log.d("CHECKER", "RegistrationActivity: Email: $email")
                        Log.d("CHECKER", "RegistrationActivity: Password: $pwd")
                        Toast.makeText(this@RegistrationActivity, "Failed to create user.", Toast.LENGTH_SHORT).show()
                        //return@addOnCompleteListener
                    }
                    //else if successful
                    else {
                        Toast.makeText(this@RegistrationActivity, "You are logged in.", Toast.LENGTH_LONG).show()
                        Log.d("CHECKER","Successfully created user with uid: ${it.result?.user?.uid}")
                        Log.d("CHECKER", "RegistrationActivity: Name: $name")
                        Log.d("CHECKER", "RegistrationActivity: Email: $email")
                        Log.d("CHECKER", "RegistrationActivity: Password: $pwd")

                        uploadImageToFirebaseStorage()

                        startActivity(Intent(this@RegistrationActivity, TaskActivity::class.java))
                    }
                }
        }
    }


}
