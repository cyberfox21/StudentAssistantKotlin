package com.tatyanashkolnik.studentassistantkotlin.addcards

import android.app.ActionBar
import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.data.NoteCard
import kotlinx.android.synthetic.main.activity_add_note.*
import java.util.*

const val REQUEST_CODE = 0

class AddNoteActivity : AppCompatActivity() {

    private lateinit var model: NoteCard
    private var selectedPhoto: Uri? = null
    private var selectedPhotoString: String = ""
    private var photoEnabled = "0"

    private lateinit var title: String
    private lateinit var subtitle: String
    private var startHours: String = ""
    private var startMinutes: String = ""
    private var endHours: String = ""
    private var endMinutes: String = ""

    private lateinit var time: String

    private lateinit var image: LottieAnimationView
    private lateinit var btnSendNote: FloatingActionButton

    private lateinit var tvAddStartTimeHours: TextView
    private lateinit var tvAddEndTimeHours: TextView
    private lateinit var tvAddStartTimeMinutes: TextView
    private lateinit var tvAddEndTimeMinutes: TextView

    private var cardRef = FirebaseDatabase.getInstance().reference.child("notes").child(FirebaseAuth.getInstance().uid.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        initFields()
        initListeners()
    }

    private fun initFields() {
        val actionBar: ActionBar? = actionBar
        actionBar?.setTitle(Html.fromHtml("<font color='#FFFFFF'>Add new note</font>"))
        btnSendNote = findViewById(R.id.btn_send_note)
        tvAddStartTimeHours = findViewById(R.id.tv_add_time_start_hours)
        tvAddStartTimeMinutes = findViewById(R.id.tv_add_time_start_minutes)
        tvAddEndTimeHours = findViewById(R.id.tv_add_time_end_hours)
        tvAddEndTimeMinutes = findViewById(R.id.tv_add_time_end_minutes)
        image = findViewById(R.id.add_image)
    }

    private fun initListeners() {
        image.setOnClickListener {
            chooseImage()
        }
        tvAddStartTimeHours.setOnClickListener {
            addTime("start")
        }
        tvAddStartTimeMinutes.setOnClickListener {
            addTime("start")
        }
        tvAddEndTimeHours.setOnClickListener {
            addTime("end")
        }
        tvAddEndTimeMinutes.setOnClickListener {
            addTime("end")
        }
        btnSendNote.setOnClickListener {
            sendNote()
        }
    }

    private fun addTime(key: String) {
        val cal = Calendar.getInstance()
        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hour, minute ->
            cal.set(Calendar.HOUR_OF_DAY, hour)
            cal.set(Calendar.MINUTE, minute)
            when (key) {
                "start" -> {
                    startHours = cal.get(Calendar.HOUR_OF_DAY).toString()
                    startMinutes = cal.get(Calendar.MINUTE).toString()
                    tvAddStartTimeHours.text = startHours
                    tvAddStartTimeMinutes.text = if (startMinutes.toInt() < 10) "0$startMinutes" else startMinutes
                }
                "end" -> {
                    endHours = cal.get(Calendar.HOUR_OF_DAY).toString()
                    endMinutes = cal.get(Calendar.MINUTE).toString()
                    tvAddEndTimeHours.text = endHours
                    tvAddEndTimeMinutes.text = if (endMinutes.toInt() < 10) "0$endMinutes" else endMinutes
                }
            }
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
                    Log.d("CHECKER", "AddNoteActivity: File location: $selectedPhotoString")
                    pushFile(selectedPhotoString)
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
        title = et_add_title.text.toString()
        subtitle = et_add_subtitle.text.toString()

        when {
            (startHours != null && endHours != null && startHours != "" && endHours != "") -> {
                time = "$startHours:$startMinutes - $endHours:$endMinutes"
            }
            (startHours != null && startHours != "") -> {
                time = "$startHours:$startMinutes"
            }
            (endHours != null && endHours != "") -> {
                time = "$endHours:$endMinutes"
            }
            else -> {
                time = ""
            }
        }

        var photoEnabled: String = when (selectedPhoto) {
            null -> "0"
            else -> "1"
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

                if (photoEnabled == "1") {
                    uploadImageToFirebaseStorage()
                } else {
                    pushFile("")
                }
            }
        }
    }

    private fun pushFile(photo: String) {
        Log.d("CHECKER", "photo $photo")

        var priority = when {
//            green_priority.isChecked -> "green"
//            yellow_priority.isChecked -> "yellow"
//            red_priority.isChecked -> "red"
            else -> ""
        }

        model = NoteCard(
            title ?: "",
            subtitle ?: "",
            time ?: "",
            photoEnabled ?: "",
            photo ?: "",
            priority ?: "",
            ""
        )

        Log.d("CHECKER", "Title: $title | Subtitle: $subtitle | Time: $time | PhotoAttached $photoEnabled | PhotoURL $photo | Priority $priority")

        FirebaseDatabase.getInstance().reference.child("notes").child(FirebaseAuth.getInstance().uid.toString()).push()
            .setValue(
                model
            )

        finish()
    }
}
