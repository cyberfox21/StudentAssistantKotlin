package com.tatyanashkolnik.studentassistantkotlin.main.passwords

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
import com.tatyanashkolnik.studentassistantkotlin.addcards.AddCardActivity
import com.tatyanashkolnik.studentassistantkotlin.data.PasswordCard

const val REQUEST_CODE = 1

class PasswordFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var rootView: View
    private lateinit var recyclerView: RecyclerView
    private lateinit var resultList: ArrayList<PasswordCard>

    private lateinit var adapter: PasswordCardAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        rootView = inflater.inflate(R.layout.fragment_password, container, false)

        initFields()
        initListeners()

        return rootView
    }

    private fun initFields() {
        resultList = ArrayList()
        fab = rootView.findViewById(R.id.password_fab)
        recyclerView = rootView.findViewById(R.id.password_card_recyclerview)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        updateList()
        adapter = PasswordCardAdapter(resultList)
        recyclerView.adapter = adapter
    }

    private fun initListeners() {
        fab.setOnClickListener { addNewPasswordCard() }
    }

    private fun addNewPasswordCard() {
        Log.d("CHECKER", "New card added")
        startActivityForResult(Intent(activity, AddCardActivity::class.java), REQUEST_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.d("CHECKER", "Password card added")
            Toast.makeText(activity, "Password card added", Toast.LENGTH_SHORT).show()
        }
    }

    private fun updateList() {
        Log.d("CHECKER", "updateList()")
        FirebaseDatabase.getInstance().reference.child("passwords").child(FirebaseAuth.getInstance().uid.toString())
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
                    resultList.add(snapshot.getValue(PasswordCard::class.java)!!)

                    adapter.notifyDataSetChanged()
                    Log.d(
                        "CHECKER",
                        "ChatActivity : ChildEventListener : onChildAdded() \n" +
                            "Service: ${snapshot.getValue(PasswordCard::class.java)!!.service} | " +
                            "Login: ${snapshot.getValue(PasswordCard::class.java)!!.login} | " +
                            "Password: ${snapshot.getValue(PasswordCard::class.java)!!.password}"
                    )
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    var message = snapshot.getValue(PasswordCard::class.java)
                    var index = message?.let { getItemIndex(it) }
                    if (index != null) {
                        resultList.removeAt(index)
                    }
                    adapter.notifyDataSetChanged()
                    Log.d("CHECKER", "ChatActivity : ChildEventListener : onChildRemoved()")
                }
            })
    }
    private fun getItemIndex(passwordCard: PasswordCard): Int {
        var index = -1
        for (i in 0 until resultList.size) {
            if (resultList[i].service == passwordCard.service) {
                index = i
                break
            }
        }
        return index
    }
}
