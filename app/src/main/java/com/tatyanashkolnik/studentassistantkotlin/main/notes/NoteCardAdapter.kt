package com.tatyanashkolnik.studentassistantkotlin.main.notes

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tatyanashkolnik.studentassistantkotlin.data.PasswordCard
import com.tatyanashkolnik.studentassistantkotlin.showcards.AdvancedCardActivity
import kotlinx.android.synthetic.main.card_password.view.*

class NoteCardAdapter(resultList: ArrayList<PasswordCard>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    enum class CellType(viewType: Int) {
        USER(0),
        PROJECT(1)
    }

    inner class NoteCardViewHolder internal constructor (itemView: View, isPhotoIn: Boolean) : RecyclerView.ViewHolder(itemView) {
        fun bind(model: PasswordCard) {
            itemView.card_service.setText(model.service)
            //itemView.card_service.movementMethod = ScrollingMovementMethod()
            Log.d("CHECKER", "PasswordAdapter: service " + model.service)
            itemView.card_login.text = model.login
            Log.d("CHECKER", "PasswordAdapter: login " + model.login)
            itemView.card_password.text = model.password
            Log.d("CHECKER", "PasswordAdapter: password " + model.password)
            itemView.setOnClickListener {
                itemView.context.startActivity(Intent(itemView.context, AdvancedCardActivity::class.java))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }
}