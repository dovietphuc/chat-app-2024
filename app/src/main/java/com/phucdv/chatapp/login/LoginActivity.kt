package com.phucdv.chatapp.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.phucdv.chatapp.MainActivity
import com.phucdv.chatapp.R
import com.phucdv.chatapp.auth.login
import com.phucdv.chatapp.databinding.ActivityLoginBinding
import com.phucdv.chatapp.forgotpassword.ForgotPasswordActivity
import com.phucdv.chatapp.register.RegisterActivity
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.loginButton.setOnClickListener {
            lifecycleScope.launch {
                login(binding.emailEditText.text.toString(), binding.passwordEditText.text.toString()).collectLatest { task ->
                    if(task.isSuccessful) {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, task.exception?.message.toString(), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.registerText.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.forgotPasswordText.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }
    }
}