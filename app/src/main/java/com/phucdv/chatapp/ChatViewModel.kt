package com.phucdv.chatapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.phucdv.chatapp.adapter.model.MessageItem
import com.phucdv.chatapp.model.Conversation
import com.phucdv.chatapp.model.Message
import com.phucdv.chatapp.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import java.util.ArrayList

class ChatViewModel : ViewModel() {

    private val _chatData = MutableStateFlow<List<MessageItem>>(emptyList())
    val chatData = _chatData.asStateFlow()

    fun getChats(myUserId: String, toUserId: String) {
        val firebaseFireStore = FirebaseFirestore.getInstance()
        firebaseFireStore.collection("message")
            .addSnapshotListener { value, error ->
                val message = ArrayList<MessageItem>()
                value?.documents?.forEach { documentSnapshot ->
                    val id = documentSnapshot.id
                    val fromUser = documentSnapshot.getString("from_user_id") ?: return@forEach
                    val toUser = documentSnapshot.getString("to_user_id") ?: return@forEach
                    val content = documentSnapshot.getString("content") ?: return@forEach
                    val createdTime = documentSnapshot.getTimestamp("created_time") ?: return@forEach

                    if((fromUser == myUserId && toUser == toUserId) || (toUser == myUserId && fromUser == toUserId)) {
                        val mesItem = if(fromUser == myUserId) {
                            MessageItem.SendMessageItem(Message(id, content, createdTime, fromUser, toUser))
                        } else {
                            MessageItem.ReceiverMessageItem(Message(id, content, createdTime, fromUser, toUser))
                        }
                        message.add(mesItem)
                    }

                }
                _chatData.value = message.sortedByDescending {
                    it.message.createdTime
                }
            }
    }

    fun pushNewMessage(message: Message) {
        val map = mapOf(
            "from_user_id" to message.fromUserId,
            "to_user_id" to message.toUserId,
            "content" to message.content,
            "created_time" to message.createdTime
        )

        val firebaseFireStore = FirebaseFirestore.getInstance()
        firebaseFireStore.collection("message")
            .add(map)
    }
}