package com.phucdv.chatapp.model

import com.google.firebase.Timestamp

data class User(
    val id: String,
    val uuid: String,
    val email: String,
    val createdTime: Timestamp
)
