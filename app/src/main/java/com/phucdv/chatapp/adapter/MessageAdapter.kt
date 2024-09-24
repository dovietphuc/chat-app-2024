package com.phucdv.chatapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.phucdv.chatapp.adapter.model.MessageItem
import com.phucdv.chatapp.databinding.MessageItemBinding

class MessageAdapter : ListAdapter<MessageItem, MessageAdapter.MessageViewHolder>(object :
    DiffUtil.ItemCallback<MessageItem>() {
    override fun areItemsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
        return oldItem.gravity == newItem.gravity
                && oldItem.backgroundRes == newItem.backgroundRes
                && oldItem.message.content == newItem.message.content
                && oldItem.message.id == newItem.message.id
                && oldItem.message.createdTime == newItem.message.createdTime
    }

    override fun areContentsTheSame(oldItem: MessageItem, newItem: MessageItem): Boolean {
        return oldItem.message.id == newItem.message.id
    }

}) {

    class MessageViewHolder(val binding: MessageItemBinding) : ViewHolder(binding.root) {
        fun bind(item: MessageItem) {
            binding.content.setBackgroundResource(item.backgroundRes)
            binding.content.layoutParams?.let {
                if(it is FrameLayout.LayoutParams) {
                    it.gravity = item.gravity
                }
            }
            binding.content.text = item.message.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        return MessageViewHolder(
            MessageItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}