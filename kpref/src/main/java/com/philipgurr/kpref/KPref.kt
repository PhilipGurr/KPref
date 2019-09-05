package com.philipgurr.kpref

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.google.gson.Gson
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KProperty

/**
 * Gets the default shared preferences for the specified context, applies the modifier and commits the changes.
 *
 * @param modify The block to execute on the corresponding shared preference editor.
 */
inline fun Context.editPreferences(modify: SharedPreferences.Editor.() -> Unit) {
    val prefs = PreferenceManager.getDefaultSharedPreferences(this).edit()
    prefs.modify()
    prefs.apply()
}

/**
 * A property delegate that saves the value directly to the shared preferences and reads from it.
 *
 * @param T The type of the element to save to the preferences.
 * @property name The key for the preference.
 * @property default The default value to return if no value is present.
 */
class SharedPreference<T : Any>(private val name: String, private val default: T) :
    ReadWriteProperty<ContextProvider, T> {
    private val gson = Gson()
    private val clazz: KClass<out T> = default::class

    /**
     * Uses the name as key and the block for building the default object.
     *
     * @param name The key for the preference.
     * @param block A function generating the default value.
     */
    constructor(name: String, block: () -> T) : this(name, block())

    /**
     * Reads the value from the preferences.
     *
     * @param thisRef A class implementing ContextProvider for getting the corresponding context.
     * @param property The property to save the value in.
     * @return The value from the preferences.
     */
    @Suppress(names = ["UNCHECKED_CAST", "IMPLICIT_CAST_TO_ANY"])
    override fun getValue(thisRef: ContextProvider, property: KProperty<*>): T {
        val prefs = PreferenceManager.getDefaultSharedPreferences(thisRef.context)
        return when (default) {
                is Int -> prefs.getInt(name, default)
                is Float -> prefs.getFloat(name, default)
                is Long -> prefs.getLong(name, default)
                is Boolean -> prefs.getBoolean(name, default)
                is String -> prefs.getString(name, default)
                else -> {
                    return try {
                        gson.fromJson(prefs.getString(name, ""), clazz.java)
                    } catch (ex: Exception) {
                        default
                    }
                }
        } as T
    }

    /**
     * Writes the value to the preferences.
     *
     * @param thisRef A class implementing ContextProvider for getting the corresponding context.
     * @param property The property to get the value from.
     * @param value The value to write to the preferences.
     */
    override fun setValue(thisRef: ContextProvider, property: KProperty<*>, value: T) {
        thisRef.context.editPreferences {
            when (value) {
                is Int -> putInt(name, value)
                is Float -> putFloat(name, value)
                is Long -> putLong(name, value)
                is Boolean -> putBoolean(name, value)
                is String -> putString(name, value)
                else -> putString(name, gson.toJson(value))
            }
        }
    }
}