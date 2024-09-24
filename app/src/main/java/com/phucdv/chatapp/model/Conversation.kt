package com.phucdv.chatapp.model

data class Conversation(
    val id: String,
    val users: List<User>,
    val message: List<Message>
)
