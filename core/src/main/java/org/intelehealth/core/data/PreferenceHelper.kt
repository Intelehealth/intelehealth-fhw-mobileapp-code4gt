package com.codeglo.coyamore.data

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager


class PreferenceHelper (private val context: Context) {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    companion object {
        const val AUTH_TOKEN: String = "AUTH_TOKEN"
    }

    fun save(key: String?, value: Any?) {
        val editor = getEditor()
        if (value is Boolean) {
            editor.putBoolean(key, (value as Boolean?)!!)
        } else if (value is Int) {
            editor.putInt(key, (value as Int?)!!)
        } else if (value is Float) {
            editor.putFloat(key, (value as Float?)!!)
        } else if (value is Long) {
            editor.putLong(key, (value as Long?)!!)
        } else if (value is String) {
            editor.putString(key, value as String?)
        } else if (value is Enum<*>) {
            editor.putString(key, value.toString())
        } else if (value != null) {
            throw RuntimeException("Attempting to save non-supported preference")
        }
        editor.commit()
    }

    fun getString(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    fun <T> get(key: String?): T {
        return sharedPreferences.all[key] as T
    }

    fun <T> get(key: String?, defValue: T): T {
        return (sharedPreferences.all[key] ?: defValue) as T
    }

    private fun getEditor(): SharedPreferences.Editor {
        return sharedPreferences.edit()
    }

    fun clear(key: String) {
        sharedPreferences.edit().run {
            remove(key)
        }.apply()
    }
}