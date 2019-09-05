package com.philipgurr.kpref

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class KPrefInstrumentedTest : ContextProvider {
    override val context: Context = InstrumentationRegistry.getInstrumentation().targetContext

    // Preferences for String
    private val stringKey = "stringKey"
    private var stringPref by SharedPreference(stringKey, "")

    // Preferences for Integers
    private val intKey = "intKey"
    private val intCalc = {5 + 5}
    private val intPref by SharedPreference(intKey, intCalc)

    // Preferences for any object.
    private val userKey = "userKey"
    private val defaultUser = User("-", "-", 0)
    private var userPref by SharedPreference(userKey, defaultUser)

    @Test
    fun testPreferenceDelegatePrimitives() {
        // Test with String
        val value1 = "Value1"
        val value2 = "Value2"
        stringPref = value1
        assertEquals(value1, stringPref)
        context.editPreferences {
            putString(stringKey, value2)
        }
        assertEquals(value2, stringPref)

        // Test with Int
        assertEquals(intCalc(), intPref)
    }

    @Test
    fun testPreferencesDelegateArbitraryObjects() {
        // Assert that the initial user is returned
        assertEquals(defaultUser, userPref)

        // Write User object to preference
        val user = User("Heinz", "Gregor", 41)
        userPref = user
        assertEquals(user, userPref)

        // Remove user
        context.editPreferences {
            remove(userKey)
        }

        // Assert that the default user is returned again
        assertEquals(defaultUser, userPref)
    }

    data class User(val surname: String,
                    val name: String,
                    val age: Int)
}
