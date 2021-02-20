package com.tatyanashkolnik.studentassistantkotlin.main.documents

import com.tatyanashkolnik.studentassistantkotlin.R
import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso
import com.tatyanashkolnik.studentassistantkotlin.data.Document
import com.tatyanashkolnik.studentassistantkotlin.main.notes.NoteCardAdapter
import kotlinx.android.synthetic.main.slider_layout.view.*
import java.io.File

class SliderAdapter(context: Context) :
    SliderViewAdapter<SliderAdapter.SliderAdapterVH>() {
    private val context: Context = context

    private var mSliderItems: MutableList<Document> = ArrayList()

    fun renewItems(sliderItems: MutableList<Document>) {
        mSliderItems = sliderItems
        notifyDataSetChanged()
    }

    fun deleteItem(position: Int) {
        mSliderItems.removeAt(position)
        notifyDataSetChanged()
    }

    fun addItem(document: Document) {
        mSliderItems.add(document)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        val inflate: View =
            LayoutInflater.from(parent.context).inflate(R.layout.slider_layout, null)
        return SliderAdapterVH(inflate)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterVH, position: Int) {
        val document: Document = mSliderItems[position]
        viewHolder.textViewDescription.text = document.title
        viewHolder.imageViewBackground.setImageBitmap(document.bitmap)
        //Picasso.get().load(Uri.parse(document.link)).into(viewHolder.imageViewBackground)



//        Glide.with(viewHolder.itemView)
//            .load(File(Uri.parse(document.link).path!!))
//            .fitCenter()
//            .into(viewHolder.imageViewBackground)
//        viewHolder.itemView.setOnClickListener(object : View.OnClickListener() {
//            fun onClick(v: View?) {
//                Toast.makeText(context, "This is item in position $position", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        })
    }

    override fun getCount(): Int {
        //slider view count could be dynamic size
        return mSliderItems.size
    }

    inner class SliderAdapterVH(itemView: View) : ViewHolder(itemView) {
        var imageViewBackground: ImageView = itemView.findViewById(R.id.iv_slider) as ImageView
        var textViewDescription: TextView = itemView.findViewById(R.id.tv_slider) as TextView
    }

}