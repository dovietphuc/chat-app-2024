package com.phucdv.chatapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.phucdv.chatapp.adapter.ConversationAdapter
import com.phucdv.chatapp.adapter.MessageAdapter
import com.phucdv.chatapp.databinding.ActivityChatBinding
import com.phucdv.chatapp.databinding.ActivityMainBinding
import com.phucdv.chatapp.login.LoginActivity
import com.phucdv.chatapp.model.Message
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private val adapter = MessageAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        verifyUser()

        enableEdgeToEdge()
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.rcvMessage.adapter = adapter
        binding.rcvMessage.layoutManager = LinearLayoutManager(this).apply {
            reverseLayout = true
        }

        val viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        lifecycleScope.launch {
            viewModel.chatData.collectLatest {
                adapter.submitList(it)
            }
        }

        val toUserId = intent.getStringExtra("extra_firestore_user_id") ?: ""
        val myUserId = intent.getStringExtra("extra_firestore_my_user_id") ?: ""
        viewModel.getChats(myUserId, toUserId)

        binding.btnBack.setOnClickListener {
            finish()
        }

        val toUserEmail = intent.getStringExtra("extra_firestore_my_user_email") ?: ""
        binding.txtTitle.text = toUserEmail

        binding.btnSend.setOnClickListener {
            viewModel.pushNewMessage(
                Message("", binding.edtMessage.text.toString(), Timestamp.now(), myUserId, toUserId)
            )
            binding.edtMessage.setText("")
        }
    }

    private fun verifyUser() {
        val firebaseUser = FirebaseAuth.getInstance().currentUser
        if(firebaseUser == null) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}