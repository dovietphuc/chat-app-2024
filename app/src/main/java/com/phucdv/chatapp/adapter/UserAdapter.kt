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
import com.phucdv.chatapp.databinding.UserItemBinding
import com.phucdv.chatapp.model.Conversation
import com.phucdv.chatapp.model.User

class UserAdapter(val onClick: (User) -> Unit) : ListAdapter<User, UserAdapter.UserViewHolder>(object :
    DiffUtil.ItemCallback<User>() {
    override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
                && oldItem.email == newItem.email
                && oldItem.createdTime == newItem.createdTime
    }

    override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
        return oldItem.id == newItem.id
    }

}) {

    inner class UserViewHolder(val binding: UserItemBinding) : ViewHolder(binding.root) {
        fun bind(item: User) {
            binding.root.text = item.email
            binding.root.setOnClickListener {
                onClick(item)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}