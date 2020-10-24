package com.tatyanashkolnik.studentassistantkotlin

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import kotlin.coroutines.coroutineContext

object Constants {

    const val SWITCHER_STATE = "theme"
    const val KEY_THEME = "theme_prefs"
    const val KEY_LANG = "lang_prefs"

    const val THEME_UNDEFINED = -1
    const val THEME_LIGHT = 0
    const val THEME_DARK = 1
}