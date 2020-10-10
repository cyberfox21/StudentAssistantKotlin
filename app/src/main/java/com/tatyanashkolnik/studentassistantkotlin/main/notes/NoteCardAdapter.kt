package com.tatyanashkolnik.studentassistantkotlin.main.notes

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.data.NoteCard
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
        if (noteList[position].photoAttached == 0) {
            0
        } else {
            1
        }

    inner class NoteCardViewHolder internal constructor (itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: NoteCard) {
            if (model.photoAttached == 0) {
                itemView.card_task_title.text = model.title
                Log.d("CHECKER", "PasswordAdapter: title " + model.title)
                itemView.card_task_subtitle.text = model.subtitle
                Log.d("CHECKER", "PasswordAdapter: login " + model.subtitle)
                itemView.card_task_time.text = model.time
                Log.d("CHECKER", "PasswordAdapter: password " + model.time)
            } else {
                itemView.card_task_image_title.text = model.title
                Log.d("CHECKER", "PasswordAdapter: title " + model.title)
                itemView.card_task_image_subtitle.text = model.subtitle
                Log.d("CHECKER", "PasswordAdapter: login " + model.subtitle)
                itemView.card_task_image_time.text = model.time
                Log.d("CHECKER", "PasswordAdapter: password " + model.time)
                itemView.card_task_image.setImageURI(Uri.parse(model.photo))
                Log.d("CHECKER", "PasswordAdapter: password " + model.photo)
            }

//            itemView.setOnClickListener {
//                itemView.context.startActivity(Intent(itemView.context, AddNoteActivity::class.java))
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            CellType.DEFAULT.ordinal ->  NoteCardViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.card_task, parent, false)
            )
            else ->  NoteCardViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.card_task_image, parent, false)
            )
        }

    override fun getItemCount(): Int =
        noteList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var note: NoteCard = noteList[position]

        (holder as NoteCardAdapter.NoteCardViewHolder).bind(note)
    }
}
