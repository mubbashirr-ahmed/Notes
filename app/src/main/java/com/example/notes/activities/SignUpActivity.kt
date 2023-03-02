package com.example.notes.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.notes.R
import com.example.notes.databinding.ActivitySignUpBinding
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {
    lateinit var binding: ActivitySignUpBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth
        clickListeners()
    }

    private fun clickListeners() {
        binding.bRegister.setOnClickListener {
            getData()
        }
    }


    private fun getData() {
        val email = binding.etUsername.text.toString().trim()
        val password = binding.etPassword.text.toString().trim()
        val cPass = binding.etCPassword.text.toString().trim()
        if (email.isEmpty() || password.isEmpty() || cPass.isEmpty()) {
            Toast.makeText(this, "Please Fill all details", Toast.LENGTH_LONG).show()
            return
        }
        if (password != cPass) {
            Toast.makeText(this, "Password Don't match!", Toast.LENGTH_LONG).show()
            return
        }
        registerUser(email, password)
    }

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(baseContext, "Account Created", Toast.LENGTH_SHORT).show()
                    finish()
                }
                else {
                    Toast.makeText(
                        baseContext,
                        task.exception?.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }
}