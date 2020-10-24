package com.tatyanashkolnik.studentassistantkotlin

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.auth.FirebaseAuth
import com.tatyanashkolnik.studentassistantkotlin.Constants.KEY_THEME
import com.tatyanashkolnik.studentassistantkotlin.Constants.SWITCHER_STATE
import com.tatyanashkolnik.studentassistantkotlin.main.TaskActivity

class Home : Application() {

    lateinit var sp : SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sp = getSharedPreferences(KEY_THEME, Context.MODE_PRIVATE)
        val state =  (sp.getInt(SWITCHER_STATE, 0))

        when(this.getSharedPreferences(Constants.KEY_THEME, Context.MODE_PRIVATE)!!.getInt(Constants.SWITCHER_STATE, 0)){
            1 -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, Constants.THEME_DARK)
            0 -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, Constants.THEME_LIGHT)
        }

        if (FirebaseAuth.getInstance().currentUser != null) {
            val intent = Intent((applicationContext), TaskActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            AppCompatDelegate.setDefaultNightMode(state)
            startActivity(intent)

        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun saveTheme(theme: Int) = sp.edit().putInt(Constants.SWITCHER_STATE, theme).apply()

    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveTheme(prefsMode)
    }
}
