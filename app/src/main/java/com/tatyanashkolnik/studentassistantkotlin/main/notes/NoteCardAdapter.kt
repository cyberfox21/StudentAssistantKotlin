package com.tatyanashkolnik.studentassistantkotlin.main.notes

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.DisplayMetrics
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.tatyanashkolnik.studentassistantkotlin.Home
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.addcards.AddNoteActivity
import com.tatyanashkolnik.studentassistantkotlin.data.NoteCard
import com.tatyanashkolnik.studentassistantkotlin.main.TaskActivity
import com.tatyanashkolnik.studentassistantkotlin.showcards.AdvancedCardActivity
import kotlinx.android.synthetic.main.card_password.view.*
import kotlinx.android.synthetic.main.card_task.view.*
import kotlinx.android.synthetic.main.card_task_image.view.*

class NoteCardAdapter(resultList: ArrayList<NoteCard>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var noteList: List<NoteCard> = resultList

    enum class CellType(viewType: Int) {
        DEFAULT(0),
        IMAGE(1)
    }

    override fun getItemViewType(position: Int): Int =
        if (noteList[position].photoAttached == "0") {
            CellType.DEFAULT.ordinal
        } else {
            CellType.IMAGE.ordinal
        }

    inner class NoteCardViewHolder internal constructor (itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: NoteCard) {
            Log.d("CHECKER", "bind()")
            if (model.photoAttached == "0") {
                itemView.card_task_title.text = model.title
                Log.d("CHECKER", "NoteAdapter: title " + model.title)
                itemView.card_task_subtitle.text = model.subtitle
                Log.d("CHECKER", "NoteAdapter: login " + model.subtitle)
                itemView.card_task_time.text = model.time
                Log.d("CHECKER", "NoteAdapter: time " + model.time)
                when(model.priority){
                    "green" -> itemView.card_task_icon.setImageResource(R.drawable.ic_priority_green)
                    "yellow" -> itemView.card_task_icon.setImageResource(R.drawable.ic_priority_yellow)
                    "red" -> itemView.card_task_icon.setImageResource(R.drawable.ic_priority_red)
                    else -> itemView.card_task_icon.visibility = View.INVISIBLE
                }
            } else {

                itemView.card_task_image_title.text = model.title
                Log.d("CHECKER", "NoteAdapter: title " + model.title)
                itemView.card_task_image_subtitle.text = model.subtitle
                Log.d("CHECKER", "NoteAdapter: login " + model.subtitle)
                itemView.card_task_image_time.text = model.time
                Log.d("CHECKER", "NoteAdapter: time " + model.time)
                //itemView.card_task_image.setImageURI(Uri.parse(model.photo))
                //Log.d("CHECKER", "NoteAdapter: password " + model.photo)
                when(model.priority){
                    "green" -> itemView.card_task_image_icon.setImageResource(R.drawable.ic_priority_green)
                    "yellow" -> itemView.card_task_image_icon.setImageResource(R.drawable.ic_priority_yellow)
                    "red" -> itemView.card_task_image_icon.setImageResource(R.drawable.ic_priority_red)
                    else -> itemView.card_task_image_icon.visibility = View.INVISIBLE
                }
                if(model.photo != "") {

                    Picasso.get()
                        .load(model.photo)
                        .resize(itemView.resources.displayMetrics.widthPixels, itemView.resources.displayMetrics.heightPixels)
                        .centerInside()
                        //.centerCrop()
                        .into(itemView.card_task_image)
                }
            }

            itemView.setOnClickListener {
                var intentToAdvancedCardActivity = Intent(itemView.context, AdvancedCardActivity::class.java)
                intentToAdvancedCardActivity.putExtra("object", model)
                itemView.context.startActivity(intentToAdvancedCardActivity)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            CellType.DEFAULT.ordinal ->  NoteCardViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.card_task, parent, false)
            )
            CellType.IMAGE.ordinal ->  NoteCardViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.card_task_image, parent, false)
            )
            else -> NoteCardViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.card_task, parent, false)
            )
        }

    override fun getItemCount(): Int =
        noteList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var note: NoteCard = noteList[position]
        for(i in noteList){
            Log.d("CHECKER", "onBindViewHolder $i")
        }


        (holder as NoteCardViewHolder).bind(note)
    }
}
