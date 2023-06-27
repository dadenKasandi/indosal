package com.kasandi.indosall

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btSignUp: Button
    private lateinit var btLogin: Button

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        auth = Firebase.auth


        etEmail = findViewById(R.id.et_email_signup)
        etPassword = findViewById(R.id.et_password_signup)
        btSignUp = findViewById(R.id.bt_sign_up_submit)
        btLogin = findViewById(R.id.bt_log_in_move)


        btSignUp.setOnClickListener {
            var email = etEmail.text.toString().trim()
            var password = etPassword.text.toString().trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            updateUI(user)
                        } else {
                            Toast.makeText(
                                baseContext,
                                "Sign Up failed. Please try again.",
                                Toast.LENGTH_SHORT
                            ).show()
                            updateUI(null)
                        }
                    }
            } else {
                Toast.makeText(
                    baseContext,
                    "Please fill in all the fields.",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }

    private fun updateUI(user:FirebaseUser?){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}