package com.tatyanashkolnik.studentassistantkotlin.main

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.tatyanashkolnik.studentassistantkotlin.R

class TaskActivity : AppCompatActivity() {

    private lateinit var btnLogout: Button
    private lateinit var ivMenu: ImageView
    private lateinit var tvMenu: TextView
    private lateinit var navView: NavigationView
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navController: NavController

//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.menu_settings, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        initViews()
        initListeners()

//        btnLogout = findViewById(R.id.logout)
//
//
//        btnLogout.setOnClickListener(View.OnClickListener {
//            FirebaseAuth.getInstance().signOut()
//            Toast.makeText(this@TaskActivity, "You are logged out.", Toast.LENGTH_SHORT).show()
//            Log.d("CHECKER","TaskActivity: User logged out successfully.")
//            startActivity(Intent(this@TaskActivity, RegistrationActivity::class.java))
//        })
    }

    private fun initViews() {
        navView = findViewById(R.id.nav_view)
        drawerLayout = findViewById(R.id.drawer)
        ivMenu = findViewById(R.id.image_menu)
        tvMenu = findViewById(R.id.text_menu)
        navController = Navigation.findNavController(this, R.id.navHostFragment)
        NavigationUI.setupWithNavController(navView, navController)
    }

    private fun initListeners() {
        ivMenu.setOnClickListener {
            onMenuImageClick()
        }
        navController.addOnDestinationChangedListener { controller, destination, _ ->  setMenuTitle(destination) }
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

    private fun onNavViewItemSelected(it: MenuItem): Boolean {
        when (it.itemId) {
            R.id.profile -> {
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT)
                Log.d("CHECKER", "onNavViewItemSelected Profile")
                true
            }
            R.id.notes -> {
                Toast.makeText(this, "Notes", Toast.LENGTH_SHORT)
                Log.d("CHECKER", "onNavViewItemSelected Notes")
                true
            }
            R.id.passwords -> {
                Toast.makeText(this, "Passwords", Toast.LENGTH_SHORT)
                Log.d("CHECKER", "onNavViewItemSelected Passwords")
                true
            }
            R.id.settings -> {
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT)
                Log.d("CHECKER", "onNavViewItemSelected Settings")
                true
            }
            R.id.about -> {
                Toast.makeText(this, "About", Toast.LENGTH_SHORT)
                Log.d("CHECKER", "onNavViewItemSelected About ")
                true
            }
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
//
//    public fun about_applications(item: MenuItem) {
//        setContentView(R.layout.notice_xml)
//    }
}
