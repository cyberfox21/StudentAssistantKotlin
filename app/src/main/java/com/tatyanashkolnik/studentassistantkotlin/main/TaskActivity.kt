package com.tatyanashkolnik.studentassistantkotlin.main

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
// import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.tatyanashkolnik.studentassistantkotlin.R
import com.tatyanashkolnik.studentassistantkotlin.data.User
import de.hdodenhof.circleimageview.CircleImageView

class TaskActivity : AppCompatActivity() {

    private lateinit var btnLogout: Button
    private lateinit var ivMenu: ImageView
    private lateinit var tvMenu: TextView
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController

    private lateinit var navUserPhoto: CircleImageView
    private lateinit var navUserName: TextView

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_settings, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        initViews()
        initListeners()
    }

    private fun initViews() {
        navView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer)
        ivMenu = findViewById(R.id.image_menu)
        tvMenu = findViewById(R.id.text_menu)
        navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(navView, navController)
        val headerLayout = navView.getHeaderView(0)
        navUserPhoto = headerLayout.findViewById(R.id.userPhoto)
        navUserName = headerLayout.findViewById(R.id.nav_name)
        setUserData()
    }

    private fun setUserData() {
        val menuListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue<User>(User::class.java)
                navUserName.text = user!!.username
                Picasso.get().load(user.profileImageUrl).into(navUserPhoto)
            }
            override fun onCancelled(databaseError: DatabaseError) {
                println("loadPost:onCancelled ${databaseError.toException()}")
            }
        }
        FirebaseDatabase.getInstance().reference.child("users").child(FirebaseAuth.getInstance().currentUser!!.uid).addListenerForSingleValueEvent(menuListener)
    }

    private fun initListeners() {
        ivMenu.setOnClickListener {
            onMenuImageClick()
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->  setMenuTitle(destination) }
//        navView.setNavigationItemSelectedListener {
//            onNavViewItemSelected(it)
//        }
    }

    private fun setMenuTitle(destination: NavDestination) {
        tvMenu.text = destination.label
    }

    private fun onMenuImageClick() {
        drawerLayout.openDrawer(GravityCompat.START)
    }
}
