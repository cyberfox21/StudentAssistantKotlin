@file:Suppress("DEPRECATION")

package com.tatyanashkolnik.studentassistantkotlin.main.documents

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContentResolverCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.textfield.TextInputEditText
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType
import com.smarteist.autoimageslider.SliderAnimations
import com.smarteist.autoimageslider.SliderView
import com.squareup.picasso.Picasso
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.data.Document
import com.tatyanashkolnik.studentassistantkotlin.data.room.File
import com.tatyanashkolnik.studentassistantkotlin.data.room.FileViewModel
import kotlinx.android.synthetic.main.dialog_documents.view.*
import kotlinx.android.synthetic.main.fragment_documents.*
import kotlinx.android.synthetic.main.slider_layout.view.*

class DocumentsFragment : Fragment() {

    private lateinit var mFileViewModel: FileViewModel
    private lateinit var sliderView: SliderView

    private var alertadd : AlertDialog? = null

    private lateinit var rootview: View
    private lateinit var image: LottieAnimationView
    private lateinit var title: TextInputEditText

    private var selectedPhoto: Uri? = null
    private var bitmap: Bitmap? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        rootview = inflater.inflate(R.layout.fragment_documents, container, false)

        sliderView = rootview.findViewById<SliderView>(R.id.imageSlider)
        mFileViewModel = ViewModelProvider(requireActivity()).get(FileViewModel::class.java)

        return rootview
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        initListeners()
    }

    private fun initViews() {

        val adapter = SliderAdapter(requireContext())
//        sliderView.autoCycleDirection = SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH
        sliderView.indicatorSelectedColor = Color.BLACK
        sliderView.indicatorUnselectedColor = Color.GRAY
//        sliderView.scrollTimeInSec = 4
        sliderView.startAutoCycle()
        // init database
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM)
        sliderView.setSliderTransformAnimation(SliderAnimations.CUBEINSCALINGTRANSFORMATION)
        sliderView.setSliderAdapter(adapter)
        val documents = ArrayList<Document>().toMutableList()
        mFileViewModel.readAllData.observe(viewLifecycleOwner, Observer { it ->
            for (i in it) {
                Log.d("CHECKER", "${i.name} ${i.bitmap}")
                documents.add(Document(i.name, i.bitmap))
                //adapter.addItem(Document(i.name, i.link))
            }
            adapter.renewItems(documents)
            adapter.notifyDataSetChanged()
        })
    }



    private fun initListeners() {
        btnAddDocument.setOnClickListener { addDocument() }
    }

    private fun pickDocumentImage() {
        val toGalary = Intent(Intent.ACTION_PICK)
        toGalary.type = "image/*"
        startActivityForResult(toGalary, 0)
    }

    private fun addDocument() {
        val alertadd: AlertDialog.Builder = AlertDialog.Builder(this.context).setCancelable(true)
        val factory = LayoutInflater.from(this.context)
        val view: View = factory.inflate(R.layout.dialog_documents, null)
        image = view.findViewById(R.id.add_document_image)
        title = view.findViewById(R.id.et_add_document_title)
        alertadd.setView(view)
        val dialog = alertadd.create()
        dialog.show()
        view.btn_new_document.setOnClickListener {
            if (view.et_add_document_title.text != null && selectedPhoto != null) {
                insertDataToDatabase()
            } else if (view.et_add_document_title.text == null) {
                view.et_add_document_title.error = "Document title is empty!"
            } else if (selectedPhoto == null) {
                Toast.makeText(view.context, "Photo not picked!", Toast.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        view.add_document_image.setOnClickListener {
            pickDocumentImage()
        }
    }

    private fun insertDataToDatabase() {
        val title = title.text.toString()
        //val photo = selectedPhoto.toString()

        val file = File(0, title, bitmap!!)

        mFileViewModel.addFile(file)

        Toast.makeText(context, "successfully added", Toast.LENGTH_SHORT).show()

        selectedPhoto = null
        bitmap = null

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && data != null) {
            selectedPhoto = data.data
            MediaStore.Images.Media.getBitmap(requireContext().contentResolver ,selectedPhoto).also { bitmap = it }
            //val absolutePath = createFile(bitmap!!)
            image.setImageBitmap(bitmap)
        }
    }


//        private fun createFile(pictureBitmap: Bitmap): String {
//        val path: String = Environment.getExternalStorageDirectory().toString()
//        var fOut: OutputStream? = null
//        val counter = 0
//        val file = java.io.File(
//            path,
//            "FitnessGirl$counter.jpg"
//        )
//        fOut = FileOutputStream(file)
//        pictureBitmap.compress(
//            Bitmap.CompressFormat.PNG,
//            85,
//            fOut
//        )
//        fOut.flush()
//        fOut.close()
//
//        MediaStore.Images.Media.insertImage(
//            requireActivity().contentResolver,
//            file.absolutePath,
//            file.name,
//            file.name
//        )
//        return file.absolutePath
//    }


    override fun onPause() {
        super.onPause()
        sliderView.stopAutoCycle()
    }

    override fun onResume() {
        super.onResume()
        sliderView.startAutoCycle()
    }
}
