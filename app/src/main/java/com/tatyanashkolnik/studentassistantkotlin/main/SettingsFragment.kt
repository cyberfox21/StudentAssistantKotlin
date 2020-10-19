@file:Suppress("DEPRECATION")

package com.tatyanashkolnik.studentassistantkotlin.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.tatyanashkolnik.studentassistantkotlin.R

const val KEY_THEME = "theme_prefs"
const val THEME_LIGHT = 0
const val THEME_DARK = 1
private var preferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var sharedPrefs  : SharedPreferences

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        sharedPrefs = activity?.getSharedPreferences(KEY_THEME, Context.MODE_PRIVATE)!!
        sharedPrefs.registerOnSharedPreferenceChangeListener(preferenceChangeListener)

        // если юзер первый раз открыл
        val hasVisited: Boolean = sharedPrefs.getBoolean("hasVisited", false)

        if (!hasVisited) {
            val e: SharedPreferences.Editor = sharedPrefs.edit()
            e.putBoolean("hasVisited", true)

            // сделать Dark Theme выключенной
            var preferenceTheme = findPreference<Preference>("theme")
            if(preferenceTheme is SwitchPreferenceCompat) preferenceTheme.isChecked = false

            // а язык - английский

            e.apply() // подтвердить изменения
        } else {


            // прочекать какая настройка
            var preferenceTheme = findPreference<Preference>("theme")
            if(preferenceTheme is SwitchPreferenceCompat) preferenceTheme.isChecked = false

        }

        preferenceChangeListener =
            SharedPreferences.OnSharedPreferenceChangeListener { sharedPreferences, key ->
                val preference = findPreference<Preference>(key)
                when(preference) {
                    is SwitchPreferenceCompat -> {
                        when(preference.isChecked){
                            true -> {
                                Log.d("CHECKER", "DarkTheme")
                                setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
                                preference.isChecked = true
                            }
                            false -> {
                                Log.d("CHECKER", "LightTheme")
                                setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
                                preference.isChecked = false
                            }
                        }
                    }
                    is ListPreference -> {

                    }
                }
            }

    }

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

    private fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveTheme(prefsMode)
    }
    private fun saveTheme(theme: Int) = sharedPrefs.edit().putInt(KEY_THEME, theme).apply()
}
