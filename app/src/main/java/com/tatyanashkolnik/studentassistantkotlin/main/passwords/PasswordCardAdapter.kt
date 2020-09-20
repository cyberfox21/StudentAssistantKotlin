package com.tatyanashkolnik.studentassistantkotlin.main.passwords

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tatyanashkolnik.studentassistantkotlin.showcards.AdvancedCardActivity
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.data.PasswordCard
import kotlinx.android.synthetic.main.card_password.view.*


class PasswordCardAdapter(resultList: ArrayList<PasswordCard>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var passwordList: List<PasswordCard> = resultList

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.card_password, parent, false)
        return PasswordHolder(view)
    }

    override fun getItemCount(): Int =
        passwordList.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var password: PasswordCard = passwordList[position]

        (holder as PasswordHolder).bind(password)
    }

    inner class PasswordHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
}
