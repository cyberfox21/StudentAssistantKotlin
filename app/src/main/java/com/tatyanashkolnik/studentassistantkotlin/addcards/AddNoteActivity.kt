package com.tatyanashkolnik.studentassistantkotlin.addcards

import android.app.*
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Html
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
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

    private lateinit var key: String

    private lateinit var image: LottieAnimationView
    private lateinit var btnSendNote: FloatingActionButton

    private lateinit var tvAddStartTimeHours: TextView
    private lateinit var tvAddEndTimeHours: TextView
    private lateinit var tvAddStartTimeMinutes: TextView
    private lateinit var tvAddEndTimeMinutes: TextView

    private lateinit var addNoteCardPriority: ImageView

    private var priority: Int = R.drawable.ic_priority_default

    private var cardRef = FirebaseDatabase.getInstance().reference.child("notes").child(FirebaseAuth.getInstance().uid.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        key = intent.getStringExtra("key")
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
        addNoteCardPriority = findViewById(R.id.addNoteCardPriority)
        image = findViewById(R.id.add_image)
        if (key == "edit") {
            et_add_title.setText(intent.getStringExtra("title"))
            et_add_subtitle.setText(intent.getStringExtra("subtitle"))
            //time = intent.getStringExtra("time")

            when(intent.getStringExtra("photoEnabled")) {
                "1" -> Picasso.get().load(intent.getStringExtra("photo")).into(image)
                else -> {}
            }
            val priority = when(intent.getStringExtra("priority")) {
                "green" -> R.drawable.ic_priority_green_gray
                "yellow" -> R.drawable.ic_priority_yellow_gray
                "red" -> R.drawable.ic_priority_red_gray
                else -> R.drawable.ic_priority_default
            }
            addNoteCardPriority.setImageResource(priority)

            val path = intent.getStringExtra("path")

        }
    }

    private fun initListeners() {
        addNoteCardPriority.setOnClickListener{
            choosePriority()
        }
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
            when(key){
                "add"->{addNewNote()}
                "edit"->{editNote()}
            }
            //addNewNote()
        }
    }

    private fun choosePriority() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.choose_priority))
            .setItems(R.array.priorities
            ) { dialog, which ->
                priority = when(which){
                    0 -> R.drawable.ic_priority_red_gray
                    1 -> R.drawable.ic_priority_yellow_gray
                    2 -> R.drawable.ic_priority_green_gray
                    else -> R.drawable.ic_priority_default
                }
                addNoteCardPriority.setImageResource(priority)
            }
        builder.create().show()
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

    private fun addNewNote() {
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

        val path = cardRef.push().key.toString()

        Log.d("CHECKER", "photo $photo")
        Log.d("CHECKER", "AddNoteActivity card path: $path")

        val priorityString = when (this.priority) {
            R.drawable.ic_priority_red_gray -> "red"
            R.drawable.ic_priority_yellow_gray -> "yellow"
            R.drawable.ic_priority_green_gray -> "green"
            else -> ""

        }

        model = NoteCard(
            title ?: "",
            subtitle ?: "",
            time ?: "",
            photoEnabled ?: "",
            photo ?: "",
            priorityString ?: "",
            path ?: ""
        )

        Log.d("CHECKER", "Title: ${model.title} | Subtitle: ${model.subtitle} " +
                "| Time: ${model.time} | PhotoAttached $photoEnabled | PhotoURL ${model.photo} " +
                "| Priority ${model.priority} | Path ${model.path}")

        cardRef.child(path).setValue(
                model
            )

        finish()
    }
    private fun editNote(){

    }


}
