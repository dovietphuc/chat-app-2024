package com.phucdv.chatapp.adapter.model

import android.view.Gravity
import com.phucdv.chatapp.R
import com.phucdv.chatapp.model.Message

sealed class MessageItem(open val message: Message, val backgroundRes: Int, val gravity: Int) {

    data class ReceiverMessageItem(override val message: Message)
        : MessageItem(message, R.drawable.message_left_bg, Gravity.START)
    data class SendMessageItem(override val message: Message)
        : MessageItem(message, R.drawable.message_right_bg, Gravity.END)

}