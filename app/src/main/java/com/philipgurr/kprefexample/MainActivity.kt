package com.philipgurr.kprefexample

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.philipgurr.kpref.ContextProvider
import com.philipgurr.kpref.SharedPreference
import com.philipgurr.kpref.editPreferences
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), ContextProvider {
    override val context: Context
        get() = applicationContext

    // Counts the times how often the app was opened
    private var appOpenedPref by SharedPreference("app_opened", 0)

    private val defaultUser = User("Name", "Surname", 0)
    private var userPref by SharedPreference("user", defaultUser)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Increase app start counter
        appOpenedPref += 1

        // Show counter
        appstartcounter.text = "You opened the app $appOpenedPref times"

        // Save values on button press
        save.setOnClickListener {
            val user =
                User(name.text.toString(), surname.text.toString(), age.text.toString().toInt())
            userPref = user

            Toast.makeText(
                this,
                "User saved. Restart the app to reload from preferences.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onResume() {
        super.onResume()

        // Show values from preferences
        if (userPref != defaultUser) {
            name.setText(userPref.name)
            surname.setText(userPref.surname)
            age.setText(userPref.age.toString())
        }
    }

    data class User(
        val name: String,
        val surname: String,
        val age: Int
    )
}


