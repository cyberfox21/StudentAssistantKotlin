package com.tatyanashkolnik.studentassistantkotlin.main

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.tatyanashkolnik.studentassistantkotlin.R
import kotlinx.android.synthetic.main.dialog_view.*
import kotlinx.android.synthetic.main.fragment_documents.*

class DocumentsFragment : Fragment() {

    private lateinit var rootview: View
    // private lateinit var btnAddDocument: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootview = inflater.inflate(R.layout.fragment_documents, container, false)
        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    private fun initViews() {
        //
    }

    private fun initListeners() {
        btnAddDocument.setOnClickListener { addDocument() }
        // dialog_imageView.setOnClickListener { pickDocumentImage() }
    }

    private fun pickDocumentImage() {
        val toGalary = Intent(Intent.ACTION_PICK)
        toGalary.type = "image/*"
        startActivityForResult(toGalary, 0)
    }

    private fun addDocument() {
        val alertadd: AlertDialog.Builder = AlertDialog.Builder(this.context)
        val factory = LayoutInflater.from(this.context)
        val view: View = factory.inflate(R.layout.dialog_view, null)
        alertadd.setView(view).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            val selectedPhoto: Uri? = data.data
            // val photo = MediaStore.wr
        }
    }
}
