package com.example.notes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.notes.R
import com.example.notes.databinding.ActivityLoginBinding
import com.example.notes.models.AllObjects.UID
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        clickListeners()
    }

    private fun clickListeners() {
        binding.bSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
        binding.blOGIN.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = binding.etUsername.text.toString().trim()
        val pass =  binding.etPassword.text.toString().trim()
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {task->
            if (task.isSuccessful) {
                Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
                val user = auth.currentUser
                UID = user?.uid.toString()
                finish()
                startActivity(Intent(this, HomeActivity::class.java))
            } else
                Toast.makeText(this, task.exception?.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
    public override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        Log.i("okkkk", "start")
        if(currentUser != null){
            UID = currentUser.uid
            finish()
            startActivity(Intent(this, HomeActivity::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        Log.i("okkkk", "resume")
    }

    override fun onRestart() {
        super.onRestart()
        auth.signOut()
        Log.i("okkkk", "restart")
    }
}