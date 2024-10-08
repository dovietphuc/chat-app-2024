package com.phucdv.chatapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.phucdv.chatapp.adapter.ConversationAdapter
import com.phucdv.chatapp.adapter.MessageAdapter
import com.phucdv.chatapp.auth.registerUserByEmail
import com.phucdv.chatapp.databinding.ActivityMainBinding
import com.phucdv.chatapp.login.LoginActivity
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter = ConversationAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verifyUser()

        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rcvConversation.adapter = adapter
        binding.rcvConversation.layoutManager = LinearLayoutManager(this)

        val user = FirebaseAuth.getInstance().currentUser
        binding.txtTitle.text = user?.email

        binding.btnNewChat.setOnClickListener {
            startActivity(Intent(this, NewChatActivity::class.java))
        }

        binding.btnLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun verifyUser() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if(firebaseUser == null) {
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()
        }
    }
}