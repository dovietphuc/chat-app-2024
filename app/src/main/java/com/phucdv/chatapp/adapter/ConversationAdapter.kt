package com.phucdv.chatapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.phucdv.chatapp.adapter.model.ConversationItem
import com.phucdv.chatapp.adapter.model.MessageItem
import com.phucdv.chatapp.databinding.ConversationItemBinding
import com.phucdv.chatapp.databinding.MessageItemBinding
import com.phucdv.chatapp.model.Conversation

class ConversationAdapter : ListAdapter<ConversationItem, ConversationAdapter.ConversationViewHolder>(object :
    DiffUtil.ItemCallback<ConversationItem>() {
    override fun areItemsTheSame(oldItem: ConversationItem, newItem: ConversationItem): Boolean {
        return oldItem.title == newItem.title
                && oldItem.conversation.id == newItem.conversation.id
    }

    override fun areContentsTheSame(oldItem: ConversationItem, newItem: ConversationItem): Boolean {
        return oldItem.conversation.id == newItem.conversation.id
    }

}) {

    class ConversationViewHolder(val binding: ConversationItemBinding) : ViewHolder(binding.root) {
        fun bind(item: ConversationItem) {
            binding.user.text = item.title
            binding.content.text = item.lastMessage
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ConversationViewHolder {
        return ConversationViewHolder(
            ConversationItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ConversationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}