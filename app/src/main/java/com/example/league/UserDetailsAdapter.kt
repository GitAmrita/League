package com.example.league

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.league.databinding.ItemViewBinding
import com.example.league.model.UserDetails
import io.getstream.avatarview.coil.loadImage

class UserDetailsAdapter(private val userDetails: List<UserDetails>):
    RecyclerView.Adapter<UserDetailsAdapter.UserDetailsViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserDetailsViewHolder {
        val binding = ItemViewBinding.inflate(LayoutInflater.from(parent.context), parent,
            false)
        return UserDetailsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserDetailsViewHolder, position: Int) {
        holder.bind(userDetails[position])
    }

    override fun getItemCount(): Int {
        return userDetails.size
    }

    inner class UserDetailsViewHolder(private val binding: ItemViewBinding) :
        RecyclerView.ViewHolder(binding.root){
            fun bind(userDetails: UserDetails) {
                binding.avatar.loadImage(userDetails.avatarUrl)
                binding.name.text = userDetails.userName
                binding.title.text = userDetails.title
                binding.body.text = userDetails.body
            }
    }
}