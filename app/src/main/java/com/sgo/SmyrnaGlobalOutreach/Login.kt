package com.sgo.SmyrnaGlobalOutreach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class Login : AppCompatActivity() {

    //variables

    private lateinit var edUsername: EditText

    private lateinit var edPassword: EditText

    private lateinit var btnLogin: Button

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edUsername = findViewById(R.id.username)

        edPassword = findViewById(R.id.password)

        btnLogin = findViewById(R.id.loginBtn)

        mAuth = FirebaseAuth.getInstance()

        btnLogin.setOnClickListener {

            loginUser()

        }

    }

    private fun loginUser() {

        try {

            val email = edUsername.text.toString().trim()

            val password = edPassword.text.toString().trim()



            if (TextUtils.isEmpty(email)) {

                Toast.makeText(this, "Please enter an email address!", Toast.LENGTH_SHORT).show()

                edUsername.requestFocus()

                return

            }

            if (TextUtils.isEmpty(password)) {

                Toast.makeText(this, "Please enter a password!", Toast.LENGTH_SHORT).show()

                edPassword.requestFocus()

                return

            }


            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    Toast.makeText(this, "Login Successful!", Toast.LENGTH_SHORT).show()

                    edUsername.setText("")

                    edPassword.setText("")

                    edUsername.requestFocus()

                    // take user to next page

                    val nav = Intent(this, AdminPanel::class.java)

                    startActivity(nav)

                } else {

                    Toast.makeText(this, "Login Unsuccessful! Please try again.", Toast.LENGTH_SHORT).show()

                    edUsername.setText("")

                    edPassword.setText("")

                    edUsername.requestFocus()

                }

            }

        } catch (eer: Exception) {

            Toast.makeText(this, "Error Occurred: " + eer.message, Toast.LENGTH_SHORT).show()

        }

    }// ends login
}