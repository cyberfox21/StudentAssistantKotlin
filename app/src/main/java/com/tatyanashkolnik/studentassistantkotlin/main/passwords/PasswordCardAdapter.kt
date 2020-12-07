package com.tatyanashkolnik.studentassistantkotlin.main.passwords

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.util.Log
import android.view.ContextThemeWrapper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.addcards.AddCardActivity
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
            // itemView.card_service.movementMethod = ScrollingMovementMethod()
            Log.d("CHECKER", "PasswordAdapter: service " + model.service)
            itemView.card_login.text = model.login
            Log.d("CHECKER", "PasswordAdapter: login " + model.login)
            itemView.card_password.text = model.password
            Log.d("CHECKER", "PasswordAdapter: password " + model.password)
            itemView.setOnClickListener {
                var intentToAddCardActivity = Intent(itemView.context, AddCardActivity::class.java)
                intentToAddCardActivity.putExtra("key", "edit")
                intentToAddCardActivity.putExtra("service", model.service)
                intentToAddCardActivity.putExtra("login", model.login)
                intentToAddCardActivity.putExtra("password", model.password)
                intentToAddCardActivity.putExtra("path", model.path)
                itemView.context.startActivity(intentToAddCardActivity)
            }

            itemView.setOnLongClickListener{
                generateDialog(itemView.context, model)
                return@setOnLongClickListener true
            }
        }
    }
    private fun generateDialog(ctx: Context, model: PasswordCard) {
        val builder = AlertDialog.Builder(ctx)
        builder.setTitle(ctx.getString(R.string.specify_deleting))
        builder.setPositiveButton(android.R.string.yes) { dialog, which ->
            (FirebaseDatabase.getInstance().reference.child("passwords").child(
                FirebaseAuth.getInstance().currentUser?.uid.toString()).child(model.path)).removeValue()
            notifyDataSetChanged()
        }

        builder.setNegativeButton(android.R.string.no) { dialog, which ->
            dialog.cancel()
        }

        builder.show()
    }

}
