package com.tatyanashkolnik.studentassistantkotlin.main.notes

import android.app.Activity
import android.app.AlertDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.addcards.AddNoteActivity
import com.tatyanashkolnik.studentassistantkotlin.addcards.REQUEST_CODE
import com.tatyanashkolnik.studentassistantkotlin.data.NoteCard
import java.util.*
import kotlin.collections.ArrayList

const val REQUEST = 1

class NotesFragment : Fragment() {
    private lateinit var dialog : AlertDialog

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
    private lateinit var et_add_title: EditText
    private lateinit var et_add_subtitle: EditText

    private lateinit var time: String

    private lateinit var key: String

    private lateinit var image: LottieAnimationView
    private lateinit var btnSendNote: MaterialButton

    private lateinit var tvAddStartTimeHours: TextView
    private lateinit var tvAddEndTimeHours: TextView
    private lateinit var tvAddStartTimeMinutes: TextView
    private lateinit var tvAddEndTimeMinutes: TextView

    private lateinit var addNoteCardPriority: ImageView

    private var priority: Int = R.drawable.ic_priority_default

    private var cardRef = FirebaseDatabase.getInstance().reference.child("notes").child(FirebaseAuth.getInstance().uid.toString())

    private lateinit var rootView: View
    private lateinit var fab: FloatingActionButton
    private lateinit var recyclerView: RecyclerView
    private lateinit var resultList: ArrayList<NoteCard>
    private lateinit var adapter: NoteCardAdapter



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(R.layout.fragment_notes, container, false)

        initFields()
        initListeners()

        return rootView
    }

    private fun initFields() {
        resultList = ArrayList()
        fab = rootView.findViewById(R.id.note_fab)
        recyclerView = rootView.findViewById(R.id.note_card_recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        updateList()
        adapter = NoteCardAdapter(resultList)
        recyclerView.adapter = adapter
    }

    private fun initListeners() {
        fab.setOnClickListener {
            NewNote()
        }
    }

    private fun NewNote() {
        //val toAddCardActivity = Intent(activity, AddNoteActivity::class.java)
        //toAddCardActivity.putExtra("key", "add")
        //startActivityForResult(toAddCardActivity, REQUEST)
        callDialog("add")
    }


    private fun updateList() {
        Log.d("CHECKER", "updateList()")
        FirebaseDatabase.getInstance().reference.child("notes").child(FirebaseAuth.getInstance().uid.toString())
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {
                    Log.d("CHECKER", "ChatActivity : ChildEventListener : onCancelled()")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.d("CHECKER", "ChatActivity : ChildEventListener : onChildMoved()")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.d("CHECKER", "ChatActivity : ChildEventListener : onChildChanged()")
                }

                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    resultList.add(snapshot.getValue(NoteCard::class.java)!!)

                    adapter.notifyDataSetChanged()
                    Log.d(
                        "CHECKER",
                        "NotesFragment : ChildEventListener : onChildAdded() \n" +
                            "Title: ${snapshot.getValue(NoteCard::class.java)!!.title} | " +
                            "Subtitle: ${snapshot.getValue(NoteCard::class.java)!!.subtitle} | " +
                            "Time: ${snapshot.getValue(NoteCard::class.java)!!.time}"
                    )
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    var message = snapshot.getValue(NoteCard::class.java)
                    var index = message?.let { getItemIndex(it) }
                    if (index != null) {
                        resultList.removeAt(index)
                    }
                    adapter.notifyDataSetChanged()
                    Log.d("CHECKER", "ChatActivity : ChildEventListener : onChildRemoved()")
                }
            })
    }

    private fun getItemIndex(noteCard: NoteCard): Int {
        var index = -1
        for (i in 0 until resultList.size) {
            if (resultList[i].title == noteCard.title) {
                index = i
                break
            }
        }
        return index
    }

    private fun callDialog(key: String) {
        val alertadd: AlertDialog.Builder = AlertDialog.Builder(this.context).setCancelable(true)
        val factory = LayoutInflater.from(this.context)
        val view: View = factory.inflate(R.layout.dialog_view, null)

        btnSendNote = view.findViewById(R.id.btn_new_document) as MaterialButton
        tvAddStartTimeHours = view.findViewById(R.id.tv_add_time_start_hours) as TextView
        tvAddStartTimeMinutes = view.findViewById(R.id.tv_add_time_start_minutes) as TextView
        tvAddEndTimeHours = view.findViewById(R.id.tv_add_time_end_hours) as TextView
        tvAddEndTimeMinutes = view.findViewById(R.id.tv_add_time_end_minutes)as TextView
        addNoteCardPriority = view.findViewById(R.id.addNoteCardPriority) as ImageView
        image = view.findViewById(R.id.add_image) as LottieAnimationView
        et_add_title = view.findViewById(R.id.et_add_title) as EditText
        et_add_subtitle = view.findViewById(R.id.et_add_subtitle) as EditText

        alertadd.setView(view)
        dialog = alertadd.create()
        dialog.show()

        addNoteCardPriority.setOnClickListener {
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
            when (key) {
                "add" -> { addNewNote() }
                "edit" -> { editNote() }
            }
        }

    }

    private fun choosePriority() {
        val builder = AlertDialog.Builder(this.context)
        builder.setTitle(getString(R.string.choose_priority))
            .setItems(
                R.array.priorities
            ) { dialog, which ->
                priority = when (which) {
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
        TimePickerDialog(activity, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
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
                    activity,
                    "Failed to upload image.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    fun addNewNote() {
        title = et_add_title.text.toString()
        subtitle = et_add_subtitle.text.toString()

        when {
            (startHours != null && endHours != null && startHours != "" && endHours != "") -> {
                startMinutes = normalizeMinutes(startMinutes)
                endMinutes = normalizeMinutes(endMinutes)
                time = "$startHours:$startMinutes - $endHours:$endMinutes"
                Log.d("CHECKER", "startMinutes" + startMinutes)
                Log.d("CHECKER", "endMinutes" + endMinutes)
            }
            (startHours != null && startHours != "") -> {
                startMinutes = normalizeMinutes(startMinutes)
                time = "$startHours:$startMinutes"
            }
            (endHours != null && endHours != "") -> {
                endMinutes = normalizeMinutes(endMinutes)
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

        Log.d(
            "CHECKER",
            "Title: ${model.title} | Subtitle: ${model.subtitle} " +
                    "| Time: ${model.time} | PhotoAttached $photoEnabled | PhotoURL ${model.photo} " +
                    "| Priority ${model.priority} | Path ${model.path}"
        )

        cardRef.child(path).setValue(
            model
        )

        dialog.dismiss()
    }

    private fun editNote() {
    }

    private fun normalizeMinutes(minutes: String): String {
        if(minutes.length == 1){
            var mins = "0$minutes"
            return mins
        }
        else{return minutes}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (REQUEST_CODE == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("CHECKER", "AddNoteActivity: Photo was selected")

            selectedPhoto = data.data
            photoEnabled = "1"
            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, selectedPhoto)
            image.setImageBitmap(bitmap)
        }
    }
}
