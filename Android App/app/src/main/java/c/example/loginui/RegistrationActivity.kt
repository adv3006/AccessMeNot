package c.example.loginui

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_registration.*

class RegistrationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        register_button.setOnClickListener {
            val name = register_username.text.toString()
            val email = register_email.text.toString()
            val password = register_password.text.toString()
            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                printMessage("Please fill out everything to register.")

                return@setOnClickListener
            }

            Log.d("Registration activity", "\nusername is $email")
            Log.d("Registration activity", "password is $password")

            // Firebase Authentication to create a user
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,
                    password)
                    .addOnCompleteListener {
                        // Failed to create
                        if (!it.isSuccessful) return@addOnCompleteListener
                        // Successful created
                        Log.d("Registration Activity",
                                "User with uid: ${it.result!!.user.uid}")
                        printMessage("Successful create account with $email")
                        val intent = Intent(this,
                                MainActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        if (password.length <= 8)
                            printMessage("Please choose a longer password.")
                        else printMessage("Invalid email address")
                    }
        }

        go_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun printMessage(message: String) {
        val notice = Toast.makeText(this, message, Toast.LENGTH_LONG)
        val noticeText = notice.view.findViewById<TextView>(android.R.id.message)
        noticeText.gravity = Gravity.CENTER or Gravity.BOTTOM
        notice.show()
    }


}