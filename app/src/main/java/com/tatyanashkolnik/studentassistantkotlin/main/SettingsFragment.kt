@file:Suppress("DEPRECATION")

package com.tatyanashkolnik.studentassistantkotlin.main

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat.recreate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.tatyanashkolnik.studentassistantkotlin.Constants.EN
import com.tatyanashkolnik.studentassistantkotlin.Constants.KEY
import com.tatyanashkolnik.studentassistantkotlin.Constants.LIST_STATE
import com.tatyanashkolnik.studentassistantkotlin.Constants.RU
import com.tatyanashkolnik.studentassistantkotlin.Constants.SWITCHER_STATE
import com.tatyanashkolnik.studentassistantkotlin.Constants.THEME_DARK
import com.tatyanashkolnik.studentassistantkotlin.Constants.THEME_LIGHT
import com.tatyanashkolnik.studentassistantkotlin.R
import java.util.*

private var preferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var sharedPrefs: SharedPreferences
    private lateinit var switcher: SwitchPreferenceCompat
    private lateinit var list: ListPreference

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        sharedPrefs = activity?.getSharedPreferences(KEY, Context.MODE_PRIVATE)!!

//        preferenceChangeListener =
//            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
//                val preference = findPreference<Preference>(key)
//                when(preference) {
//                    is SwitchPreferenceCompat -> {
//                        when(preference.isChecked){
//                            true -> {
//                                Log.d("CHECKER", "DarkTheme")
//                                savePreferences(SWITCHER_STATE, 1)
//                                setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
//                            }
//                            false -> {
//                                Log.d("CHECKER", "LightTheme")
//                                savePreferences(SWITCHER_STATE, 0)
//                                setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
//                            }
//                        }
//                    }
//                    is ListPreference -> {
//
//                    }
//                }
//            }

        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)

        switcher =
            (findPreference(SWITCHER_STATE) as SwitchPreferenceCompat?)!!

        list = (findPreference(LIST_STATE) as ListPreference?)!!

        switcher.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, isChecked ->

                val checked = isChecked as Boolean
                if (checked) {
                    Log.d("CHECKER", "DarkTheme")
                    savePreferences(SWITCHER_STATE, 1)
                    setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
                } else {
                    Log.d("CHECKER", "LightTheme")
                    savePreferences(SWITCHER_STATE, 0)
                    setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
                }

                true
            }

        list.onPreferenceChangeListener =
            Preference.OnPreferenceChangeListener { _, language ->
                when (language) {
                    "English" -> {
                        Log.d("CHECKER", "English language")
                        saveLanguage(0)
                        var localeEN = Locale(EN)
                        setLocale(localeEN)
                    }
                    "Русский" -> {
                        Log.d("CHECKER", "Русский language")
                        saveLanguage(1)
                        var localeRU = Locale(RU)
                        setLocale(localeRU)
                    }
                }
                true
            }

        initSwitcher()
        initList()

        // если юзер первый раз открыл
        // val hasVisited: Boolean = sharedPrefs.getBoolean("hasVisited", false)

//        if (!hasVisited) { //первый раз
//            val e: SharedPreferences.Editor = sharedPrefs.edit()
//            e.putBoolean("hasVisited", true)
//            e.apply() // подтвердить изменения
//
//
//            //loadPreferences()
//
//            // сделать Dark Theme выключенной
//
//            switcher.isChecked = false
//
//            // а язык - английский
//
//
//
//        }
    }

    private fun initList() {
        var lang = sharedPrefs.getInt(LIST_STATE, 0)
        var locale = if (lang == 0) Locale(EN) else Locale(RU)
        var current = resources.configuration.locale
        if (locale != current) {
            when (lang) {
                0 -> {
                    saveLanguage(0)
                    setLocale(Locale(EN))
                }
                1 -> {
                    saveLanguage(1)
                    setLocale(Locale(RU))
                }
            }
        }
    }

//    private fun initList() {
//        var lang = sharedPrefs.getInt(SWITCHER_STATE, 0) == 1
//        when(isChecked){
//            true -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
//            false -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
//        }
//    }

    private fun initSwitcher() {
        var isChecked = sharedPrefs.getInt(SWITCHER_STATE, 0) == 1
        when (isChecked) {
            true -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
            false -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
        }
    }

    private fun savePreferences(key: String, value: Int) {
        val editor = sharedPrefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

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
        // activity?.applyOverrideConfiguration(conf)
        res.updateConfiguration(conf, dm)
        activity?.recreate()
    }

    private fun saveTheme(theme: Int) =
        sharedPrefs.edit().putInt(SWITCHER_STATE, theme).apply()

    private fun saveLanguage(language: Int) =
        sharedPrefs.edit().putInt(LIST_STATE, language).apply()

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
            .registerOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
            .unregisterOnSharedPreferenceChangeListener(preferenceChangeListener)
    }

//    override fun applyOverrideConfiguration(overrideConfiguration: Configuration?) {
//        overrideConfiguration?.let {
//            val uiMode = it.uiMode
//            it.setTo(resources.configuration)
//            it.uiMode = uiMode
//        }
//        super.applyOverrideConfiguration(overrideConfiguration)
//    }
}
