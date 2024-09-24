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
import com.phucdv.chatapp.adapter.UserAdapter
import com.phucdv.chatapp.databinding.ActivityNewChatBinding
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NewChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityNewChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val viewModel = ViewModelProvider(this)[UserViewModel::class.java]

        val adapter = UserAdapter {
            startActivity(Intent(this, ChatActivity::class.java).apply {
                putExtra("extra_firestore_user_id", it.id)
                putExtra("extra_firestore_my_user_id", viewModel.myUserId())
                putExtra("extra_firestore_my_user_email", it.email)
            })
        }
        binding.rcvUser.adapter = adapter
        binding.rcvUser.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            viewModel.allUser.collectLatest {
                adapter.submitList(it)
            }
        }
    }
}