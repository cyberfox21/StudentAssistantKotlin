@file:Suppress("DEPRECATION")

package com.tatyanashkolnik.studentassistantkotlin.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreferenceCompat
import com.tatyanashkolnik.studentassistantkotlin.Constants.KEY_THEME
import com.tatyanashkolnik.studentassistantkotlin.Constants.SWITCHER_STATE
import com.tatyanashkolnik.studentassistantkotlin.Constants.THEME_DARK
import com.tatyanashkolnik.studentassistantkotlin.Constants.THEME_LIGHT
import com.tatyanashkolnik.studentassistantkotlin.R


private var preferenceChangeListener: SharedPreferences.OnSharedPreferenceChangeListener? = null

class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var sharedPrefs  : SharedPreferences
    private lateinit var switcher : SwitchPreferenceCompat

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)

        sharedPrefs = activity?.getSharedPreferences(KEY_THEME, Context.MODE_PRIVATE)!!

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

        //switcher = findPreference<Preference>(SWITCHER_STATE) as SwitchPreferenceCompat

        switcher =
            (findPreference(SWITCHER_STATE) as SwitchPreferenceCompat?)!!

        if (switcher != null) {
            switcher.setOnPreferenceChangeListener(Preference.OnPreferenceChangeListener { _, isChecked ->

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

//                when(switcher.isChecked){
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
                true
            })
        }




        initSwitcher()

        // если юзер первый раз открыл
        //val hasVisited: Boolean = sharedPrefs.getBoolean("hasVisited", false)

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



//            var preferenceTheme = findPreference<Preference>("theme")
//            if(preferenceTheme is SwitchPreferenceCompat) preferenceTheme.isChecked = false





    }

    private fun initSwitcher() {
//        when (resources.configuration.uiMode.and(Configuration.UI_MODE_NIGHT_MASK)) {
//            Configuration.UI_MODE_NIGHT_NO -> switcher.isChecked = false
//            Configuration.UI_MODE_NIGHT_YES -> switcher.isChecked = true
//            Configuration.UI_MODE_NIGHT_UNDEFINED -> switcher.isChecked = false
//        }
        switcher.isChecked = sharedPrefs.getInt(SWITCHER_STATE, 0) == 1
        when(switcher.isChecked){
            true -> setTheme(AppCompatDelegate.MODE_NIGHT_YES, THEME_DARK)
            false -> setTheme(AppCompatDelegate.MODE_NIGHT_NO, THEME_LIGHT)
        }
    }

    private fun savePreferences(key: String, value: Int) {
        val editor = sharedPrefs.edit()
        editor.putInt(key, value)
        editor.apply()
    }

    public fun setTheme(themeMode: Int, prefsMode: Int) {
        AppCompatDelegate.setDefaultNightMode(themeMode)
        saveTheme(prefsMode)
    }
    private fun saveTheme(theme: Int) = sharedPrefs.edit().putInt(SWITCHER_STATE, theme).apply()



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


}
