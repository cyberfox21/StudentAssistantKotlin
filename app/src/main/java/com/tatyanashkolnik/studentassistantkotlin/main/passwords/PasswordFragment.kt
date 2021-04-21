package com.tatyanashkolnik.studentassistantkotlin.main.passwords

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
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
import kotlinx.android.synthetic.main.dialog_documents.view.*

const val REQUEST_CODE = 1

class PasswordFragment : Fragment() {
    private lateinit var dialog : AlertDialog

    private lateinit var btnSend: com.google.android.material.button.MaterialButton
    private lateinit var etService: EditText
    private lateinit var etLogin: EditText
    private lateinit var etPassword: EditText

    private lateinit var model: PasswordCard

    private var cardRef = FirebaseDatabase.getInstance().reference.child("passwords").child(FirebaseAuth.getInstance().uid.toString())

    private lateinit var key: String

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
        //Log.d("CHECKER", "New card added")
       // var intentToAddCardActivity = Intent(activity, AddCardActivity::class.java)
        //intentToAddCardActivity.putExtra("key", "add")
        //startActivityForResult(intentToAddCardActivity, REQUEST_CODE)
        callDialog("add")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            Log.d("CHECKER", "Password card added")
            Toast.makeText(activity, "Password card added", Toast.LENGTH_SHORT).show()
        }
        adapter.notifyDataSetChanged()
    }

    private fun updateList() {
        Log.d("CHECKER", "updateList()")
        FirebaseDatabase.getInstance().reference.child("passwords").child(FirebaseAuth.getInstance().uid.toString())
            .addChildEventListener(object : ChildEventListener {
                override fun onCancelled(error: DatabaseError) {
                    adapter.notifyDataSetChanged()
                    Log.d("CHECKER", "PasswordFragment : ChildEventListener : onCancelled()")
                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                    adapter.notifyDataSetChanged()
                    Log.d("CHECKER", "PasswordFragment : ChildEventListener : onChildMoved()")
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                    Log.d("CHECKER", "PasswordFragment : ChildEventListener : onChildChanged()")

                    var updatedPasswordCard = snapshot.getValue(PasswordCard::class.java)!!

                    for (i in 0 until resultList.size) {
                        if (resultList[i].path == updatedPasswordCard.path) {
                            resultList[i] = updatedPasswordCard
                        }
                    }
                    adapter.notifyDataSetChanged()
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

    private fun callDialog(key: String) {


        val alertadd: AlertDialog.Builder = AlertDialog.Builder(this.context).setCancelable(true)
        val factory = LayoutInflater.from(this.context)
        val view: View = factory.inflate(R.layout.dialog_password, null)

        btnSend = view.findViewById(R.id.fabSendPassword)
        etService = view.findViewById(R.id.etService)
        etLogin = view.findViewById(R.id.etLogin)
        etPassword = view.findViewById(R.id.etPassword)

//        if (key == "edit") {
//            etService.setText(intent.getStringExtra("service"))
//            etLogin.setText(intent.getStringExtra("login"))
//            etPassword.setText(intent.getStringExtra("password"))
//        }

        alertadd.setView(view)
        dialog = alertadd.create()
        dialog.show()

        btnSend.setOnClickListener {
            when (key) {
                "edit" -> changePasswordCard()
                "add" -> createPasswordCard()
            }
        }
    }

    private fun changePasswordCard() {
//        var path = intent.getStringExtra("path")
//
//        model = PasswordCard(
//            etService.text.toString() ?: "",
//            etLogin.text.toString() ?: "",
//            etPassword.text.toString() ?: "",
//            path
//        )
//
//        Log.d(
//            "CHECKER",
//            "PasswordCard Added" +
//                    "Service: ${model.service} | Login: ${model.login} | Password: ${model.password} | Path: ${model.path}"
//        )
//
//        cardRef.child(path).setValue(
//            model
//        )
//
//        dialog.dismiss()
    }


    private fun createPasswordCard() {
        var path = cardRef.push().key.toString()
        Log.d("T", "AddCardActivity card path: $path")

        model = PasswordCard(
            etService.text.toString() ?: "",
            etLogin.text.toString() ?: "",
            etPassword.text.toString() ?: "",
            path ?: ""
        )

        Log.d(
            "CHECKER",
            "PasswordCard Added" +
                    "Service: ${model.service} | Login: ${model.login} | Password: ${model.password} | Path: ${model.path}"
        )

        cardRef.child(path).setValue(
            model
        )

        dialog.dismiss()
    }
}
