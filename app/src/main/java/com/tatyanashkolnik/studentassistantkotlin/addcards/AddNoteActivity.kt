package com.tatyanashkolnik.studentassistantkotlin.addcards

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.data.NoteCard
import com.tatyanashkolnik.studentassistantkotlin.data.PasswordCard
import kotlinx.android.synthetic.main.activity_add_note.*
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*

const val REQUEST_CODE = 0

class AddNoteActivity : AppCompatActivity() {

    private lateinit var model: NoteCard
    private var selectedPhoto : Uri? = null
    private var selectedPhotoString : String = ""
    private var photoEnabled = "0"

    //private lateinit var title: EditText
    //private lateinit var subtitle: EditText
    //private lateinit var priority: RadioButton
    private lateinit var image: LottieAnimationView
    private lateinit var btnSendNote: FloatingActionButton
    private lateinit var btnAddTime: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)

        initFields()
        initListeners()

    }

    private fun initFields() {
        btnSendNote = findViewById(R.id.btn_send_note)
        btnAddTime = findViewById(R.id.btn_add_time)
        image = findViewById(R.id.add_image)
    }

    private fun initListeners() {
        image.setOnClickListener {
            chooseImage()
        }
        btnAddTime.setOnClickListener {
            addTime()
        }
        btnSendNote.setOnClickListener {
            sendNote()
        }
    }

    private fun addTime() {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
        }
        TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
    }

    private fun chooseImage() {
        Log.d("CHECKER", "AddNoteActivity: Try to show photo galary")
        val toGalary = Intent(Intent.ACTION_PICK)
        toGalary.type = "image/*"
        startActivityForResult(toGalary, 0)
    }

    private fun uploadImageToFirebaseStorage() {
        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/notes/$filename")
        ref.putFile(selectedPhoto!!)
            .addOnSuccessListener {
                Log.d(
                    "CHECKER",
                    "AddNoteActivity: Successfully uploaded image: ${it.metadata?.path}"
                )

                ref.downloadUrl.addOnSuccessListener {
                    selectedPhotoString = it.toString()
                    Log.d("CHECKER", "AddNoteActivity: File location: $it")
                }
            }
            .addOnFailureListener {
                Log.d("CHECKER", "AddNoteActivity: Failed to upload image.")
                Toast.makeText(
                    this@AddNoteActivity,
                    "Failed to upload image.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (REQUEST_CODE == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("CHECKER", "AddNoteActivity: Photo was selected")

            selectedPhoto = data.data
            photoEnabled = "1"
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhoto)
            image.setImageBitmap(bitmap)
        }
    }

    private fun sendNote() {
        var title = et_add_title.text.toString()
        var subtitle = et_add_subtitle.text.toString()
        var time = tv_add_time_end_minutes.text.toString()
        var photoEnabled: String = when{
            selectedPhoto == null -> "0"
            else -> "1"
        }
        var photo = when{
            photoEnabled == "1" -> selectedPhotoString
            else -> ""
        }
        var priority = when{
            green_priority.isChecked -> "green"
            yellow_priority.isChecked -> "yellow"
            red_priority.isChecked -> "red"
            else -> ""
        }
        when {
            title.isEmpty() -> {
                Log.d("CHECKER", "AddNoteActivity: title is empty")
                et_add_title.error = "Title is empty"
                et_add_title.requestFocus()
            }
            subtitle.isEmpty() -> {
                Log.d("CHECKER", "AddNoteActivity: subtitle is empty")
                et_add_subtitle.error = "Subtitle is empty"
                et_add_subtitle.requestFocus()
            }
//            time.isEmpty() -> {
//                Log.d("CHECKER", "AddNoteActivity: title is empty")
//                Toast.makeText(this, "Please pick the time", Toast.LENGTH_SHORT).show()
//            }
            else -> {

                if(photoEnabled == "1"){
                    uploadImageToFirebaseStorage()
                }

                model = NoteCard(
                    title ?: "",
                    subtitle ?: "",
                    time ?: "",
                    photoEnabled ?: "",
                    selectedPhotoString ?: "",
                    priority ?: ""
                )

                Log.d("CHECKER", "Title: $title | Subtitle: $subtitle | Time: $time | PhotoAttached $photoEnabled | PhotoURL $photo | Priority $priority")

                FirebaseDatabase.getInstance().reference.child("note").child(FirebaseAuth.getInstance().uid.toString()).push()
                    .setValue(
                        model
                    )

                finish()
            }
        }
    }
}
