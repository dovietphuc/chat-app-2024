package com.phucdv.chatapp.register

import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.phucdv.chatapp.R
import com.phucdv.chatapp.auth.registerUserByEmail
import com.phucdv.chatapp.databinding.ActivityRegisterBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.loginText.setOnClickListener {
            finish()
        }

        binding.registerButton.setOnClickListener {
            val password = binding.passwordEditText.text.toString()
            val confirmPassword = binding.confirmPasswordEditText.text.toString()
            if(password != confirmPassword) {
                Toast.makeText(this, "Confirm password incorrect", Toast.LENGTH_SHORT).show()
            } else {
                lifecycleScope.launch {
                    val email = binding.emailEditText.text.toString()
                    registerUserByEmail(email, password).collectLatest { task ->
                        if(task.isSuccessful) {
                            Toast.makeText(this@RegisterActivity,
                                "Regis new user success with email : $email", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this@RegisterActivity,
                                task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                        }

                    }
                }
            }
        }
    }
}