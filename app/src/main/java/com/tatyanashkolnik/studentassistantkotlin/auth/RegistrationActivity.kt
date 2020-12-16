package com.tatyanashkolnik.studentassistantkotlin.auth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.tatyanashkolnik.studentassistantkotlin.Constants
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.data.User
import com.tatyanashkolnik.studentassistantkotlin.main.TaskActivity
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.UUID

class RegistrationActivity : Activity() {

    private lateinit var selectUserPhoto: ImageView

    private lateinit var btnRegister: TextView
    private lateinit var toEnter: TextView

    private lateinit var name: String
    private lateinit var email: String
    private lateinit var pwd: String

    private lateinit var sharedPrefs: SharedPreferences

    private var selectedPhotoUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        setContentView(R.layout.activity_registration)

        sharedPrefs = this.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE)

        when (this.getSharedPreferences(Constants.KEY, Context.MODE_PRIVATE)!!.getInt(Constants.SWITCHER_STATE, 0)) {
            1 -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, Constants.THEME_DARK)
            0 -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, Constants.THEME_LIGHT)
        }

        btnRegister = findViewById(R.id.btnRegister)
        selectUserPhoto = findViewById(R.id.userPhoto)
        btnRegister.setOnClickListener {
            registerUser()
        }

        toEnter = findViewById(R.id.toEnter)!!

        toEnter.setOnClickListener {
            startActivity(Intent(this@RegistrationActivity, LoginActivity::class.java))
        }

        selectUserPhoto.setOnClickListener {
            Log.d("CHECKER", "RegistrationActivity: Try to show photo galary")
            val toGalary = Intent(Intent.ACTION_PICK)
            toGalary.type = "image/*"
            startActivityForResult(toGalary, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("CHECKER", "RegistrationActivity: Photo was selected")

            selectedPhotoUri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            selectUserPhoto.setImageBitmap(bitmap)
        }
    }

    private fun uploadImageToFirebaseStorage() {

        if (selectedPhotoUri == null) {    // ///// upload stock user avatar
            val stock_photo_location = "com.google.android.gms.tasks.zzu@f6382a9"
            saveUserToDatabase(stock_photo_location)
            Log.d("CHECKER", "RegistrationActivity: Setted a stock user photo")
        } else {   // // upload selected user avatar
            val filename = UUID.randomUUID().toString()
            val ref = FirebaseStorage.getInstance().getReference("/avatars/$filename")
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d(
                        "CHECKER",
                        "RegistrationActivity: Successfully uploaded image: ${it.metadata?.path}"
                    )

                    ref.downloadUrl.addOnSuccessListener {
                        Log.d("CHECKER", "RegistrationActivity: File location: $it")
                        saveUserToDatabase(it.toString())
                    }
                }
                .addOnFailureListener {
                    Log.d("CHECKER", "RegistrationActivity: Failed to upload image.")
                    Toast.makeText(
                        this@RegistrationActivity,
                        "Failed to upload image.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            saveUserToDatabase(selectedPhotoUri!!.toString())
        }
    }

    private fun saveUserToDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        val user = User(uid, etName.text.toString(), etRegistrationPassword.text.toString(), profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("CHECKER", "RegistrationActivity: User saved in Firebase.")
            }
            .addOnFailureListener {
                Log.d("CHECKER", "RegistrationActivity: Failed to save user in Firebase.")
            }
    }

    private fun registerUser() {
        name = etName.text.toString()
        email = etEmail.text.toString()
        pwd = etRegistrationPassword.text.toString()
        when {
            name.isEmpty() -> {
                Log.d("CHECKER", "RegistrationActivity: Please enter name.")
                etName.error = "Please enter name."
                etName.requestFocus()
            }
            email.isEmpty() -> {
                Log.d("CHECKER", "RegistrationActivity: Please enter email.")
                etEmail.error = "Please enter email."
                etEmail.requestFocus()
            }
            pwd.isEmpty() -> {
                Log.d("CHECKER", "RegistrationActivity: Please enter password.")
                etPassword.error = "Please enter password."
                etPassword.requestFocus()
            }
            pwd.length < 6 -> {
                Log.d("CHECKER", "RegistrationActivity: Password must be at least 6 characters.")
                etPassword.error = "Password must be at least 6 characters."
                etPassword.requestFocus()
            }
            email.isEmpty() && pwd.isEmpty() -> {
                Log.d("CHECKER", "RegistrationActivity: Fields are empty!")
                Toast.makeText(this@RegistrationActivity, "Fields are empty!", Toast.LENGTH_SHORT).show()
            }
            !(email.isEmpty() && pwd.isEmpty()) -> {
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
                            // return@addOnCompleteListener
                        }
                        // else if successful
                        else {
                            // Toast.makeText(this@RegistrationActivity, "You are logged in.", Toast.LENGTH_LONG).show()
                            Log.d("CHECKER", "RegistrationActivity: Successfully created user with uid: ${it.result?.user?.uid}")
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
    private fun saveTheme(theme: Int) =
        sharedPrefs.edit().putInt(Constants.SWITCHER_STATE, theme).apply()

    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveTheme(prefsMode)
    }
}
