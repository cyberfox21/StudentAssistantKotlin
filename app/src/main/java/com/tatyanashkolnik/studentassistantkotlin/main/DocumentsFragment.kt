package com.tatyanashkolnik.studentassistantkotlin.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tatyanashkolnik.studentassistantkotlin.R

class DocumentsFragment : Fragment() {

    private lateinit var rootview : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View {

        rootview = inflater.inflate(R.layout.fragment_documents, container, false)
        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}