package com.sgo.SmyrnaGlobalOutreach

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth

class Register : AppCompatActivity() {
    private lateinit var edUserName: EditText

    private lateinit var edPassword: EditText

    private lateinit var edConfirmPassword: EditText

    private lateinit var btnRegister: Button

    private lateinit var btnLogin: Button

    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edUserName = findViewById(R.id.username)

        edPassword = findViewById(R.id.password)

        edConfirmPassword = findViewById(R.id.confirm_password)

        btnRegister = findViewById(R.id.registerBtn)

        mAuth = FirebaseAuth.getInstance()

    }

    // reg btn method
    fun regUser(view: View) {

        if (view.id == R.id.registerBtn) {

            val email = edUserName.text.toString().trim()

            val passwordReg = edPassword.text.toString().trim()

            val conPassword = edConfirmPassword.text.toString().trim()



            if (TextUtils.isEmpty(email)) {

                Toast.makeText(this, "Email can't be blank", Toast.LENGTH_SHORT).show()

                return

            }

            if (TextUtils.isEmpty(passwordReg)) {

                Toast.makeText(this, "Password can't be blank", Toast.LENGTH_SHORT).show()

                return

            }

            if (TextUtils.isEmpty(conPassword)) {

                Toast.makeText(this, "Confirm password can't be blank", Toast.LENGTH_SHORT).show()

                return

            }



            if (conPassword == passwordReg) {

                mAuth.createUserWithEmailAndPassword(email, passwordReg)

                    .addOnCompleteListener(this, OnCompleteListener<AuthResult> { task ->

                        if (task.isSuccessful) {

                            val intent = Intent(this@Register, AdminPanel::class.java)

                            Toast.makeText(this@Register, "Admin registered successfully.", Toast.LENGTH_SHORT).show()

                            startActivity(intent)

                            finish()


                        } else {

                            Toast.makeText(this@Register, "FAILED TO REG", Toast.LENGTH_SHORT).show()

                        }

                    })
            }
        }
    }  // end regUser
}