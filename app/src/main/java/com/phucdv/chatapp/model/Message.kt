package com.phucdv.chatapp.model

import com.google.firebase.Timestamp

data class Message(
    val id: String,
    val content: String,
    val createdTime: Timestamp,
    val fromUserId: String,
    val toUserId: String
)
