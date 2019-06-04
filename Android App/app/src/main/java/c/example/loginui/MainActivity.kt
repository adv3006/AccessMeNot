package c.example.loginui

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_button.setOnClickListener {
            val email = login_email.text.toString()
            val password = login_password.text.toString()

            // No input, toast it with nice margin msg!
            if (email.isEmpty()) {
                login_email?.error = " Missing email"
                return@setOnClickListener
            }
            if (password.isEmpty()) {
                login_password?.error = " Missing password"
                return@setOnClickListener
            }
//            Log.d("Main activity", "\nusername is $email")
//            Log.d("Main activity", "password is $password")
//            Log.d("Main activity", "Process to next activity...")

            // Firebase authentication
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
                    .addOnSuccessListener {
                        Log.d("Main activity", "Processing...")
                        login_progress_bar.visibility = View.VISIBLE
                        val intent = Intent(this,
                                ControlActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener {
                        printErrorMessage ("Incorrect email/password.")
                    }

        }
        register.setOnClickListener {
            Log.d("Main acitivity", "Proceed to registration...")
            val intent = Intent ( this,
                    RegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    private fun printErrorMessage(message: String) {
        val notice = Toast.makeText(this, message, Toast.LENGTH_LONG)
        val noticeText = notice.view.findViewById<TextView>(android.R.id.message)
        noticeText.gravity = Gravity.CENTER or Gravity.BOTTOM
        notice.show()
    }
}
