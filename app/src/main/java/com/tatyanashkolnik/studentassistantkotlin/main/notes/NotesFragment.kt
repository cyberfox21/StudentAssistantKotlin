package com.tatyanashkolnik.studentassistantkotlin.main.notes

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.addcards.AddNoteActivity
import com.tatyanashkolnik.studentassistantkotlin.data.NoteCard
import com.tatyanashkolnik.studentassistantkotlin.main.passwords.REQUEST_CODE

const val REQUEST_CODE = 1

class NotesFragment : Fragment() {

    lateinit var rootView: View
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

    fun initFields() {
        resultList = ArrayList()
        fab = rootView.findViewById(R.id.note_fab)
        recyclerView = rootView.findViewById(R.id.note_card_recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        updateList()
        adapter = NoteCardAdapter(resultList)
        recyclerView.adapter = adapter
    }

    fun initListeners() {
        fab.setOnClickListener {
            addNewNote()
        }
    }

    private fun addNewNote() {
        startActivityForResult(Intent(activity, AddNoteActivity::class.java), REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.d("CHECKER", "Note card added")
            Toast.makeText(activity, "Note card added", Toast.LENGTH_SHORT).show()
        }
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
                        "ChatActivity : ChildEventListener : onChildAdded() \n" +
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
                    // adapter.notifyDataSetChanged()
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
}
