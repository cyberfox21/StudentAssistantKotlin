package com.tatyanashkolnik.studentassistantkotlin

import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.auth.FirebaseAuth
import com.tatyanashkolnik.studentassistantkotlin.Constants.EN
import com.tatyanashkolnik.studentassistantkotlin.Constants.KEY
import com.tatyanashkolnik.studentassistantkotlin.Constants.LIST_STATE
import com.tatyanashkolnik.studentassistantkotlin.Constants.RU
import com.tatyanashkolnik.studentassistantkotlin.Constants.SWITCHER_STATE
import com.tatyanashkolnik.studentassistantkotlin.Constants.THEME_DARK
import com.tatyanashkolnik.studentassistantkotlin.Constants.THEME_LIGHT
import com.tatyanashkolnik.studentassistantkotlin.auth.RegistrationActivity
import com.tatyanashkolnik.studentassistantkotlin.main.TaskActivity
import java.util.*

@Suppress("DEPRECATION")
class Home : Application() {

    lateinit var sp: SharedPreferences

    override fun onCreate() {
        super.onCreate()

        sp = getSharedPreferences(KEY, Context.MODE_PRIVATE)
        val state = sp.getInt(SWITCHER_STATE, 0)

        // sign in
        if (FirebaseAuth.getInstance().currentUser != null) { // load TaskActivity
            val intent = Intent((applicationContext), TaskActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            when (state) {
                1 -> {
                    saveTheme(THEME_DARK)
                    setTheme(AppCompatDelegate.MODE_NIGHT_YES, Constants.THEME_DARK)
                }
                0 -> {
                    saveTheme(THEME_LIGHT)
                    setTheme(AppCompatDelegate.MODE_NIGHT_NO, Constants.THEME_LIGHT)
                }
            }
            initList()
            startActivity(intent)
        }
        /* not sign in */
        else { // load RegistationActivity
            val intent = Intent((applicationContext), RegistrationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            saveTheme(THEME_LIGHT)
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            saveLanguage(0)
            setLocale(Locale(EN))
            startActivity(intent)
        }
    }

    private fun saveTheme(theme: Int) =
        sp.edit().putInt(SWITCHER_STATE, theme).apply()

    private fun saveLanguage(language: Int) =
        sp.edit().putInt(LIST_STATE, language).apply()

    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveTheme(prefsMode)
    }

    private fun setLocale(locale: Locale) {
        Locale.setDefault(locale)
        val res: Resources = resources
        val dm: DisplayMetrics = res.displayMetrics
        val conf: Configuration = res.configuration
        conf.locale = locale
        res.updateConfiguration(conf, dm)
    }

    private fun initList() {
        var lang = sp.getInt(LIST_STATE, 0)
        var locale = if (lang == 0) Locale(EN) else Locale(RU)
        var current = resources.configuration.locale
        if (locale != current) {
            when (lang) {
                0 -> {
                    saveLanguage(0)
                    setLocale(locale)
                }
                1 -> {
                    saveLanguage(1)
                    setLocale(locale)
                }
            }
        }
    }
}
