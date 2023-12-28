package com.example.testingapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.view.menu.MenuView.ItemView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.testingapp.R
import com.example.testingapp.model.DataUser
import com.example.testingapp.screen.SecondActivity

class UserAdapter (private var users: List<DataUser>) : RecyclerView.Adapter<UserAdapter.ViewHolder>() {
    var currentData: List<DataUser> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.user_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserAdapter.ViewHolder, position: Int) {
        val user = users[position]
        holder.bind(user)
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, SecondActivity::class.java)
            intent.putExtra("first_name", user.first_name)
            intent.putExtra("last_name", user.last_name)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = users.size

    fun updateData(newData: List<DataUser>){
        users = newData
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: DataUser) {
            itemView.findViewById<TextView>(R.id.tvFirstName).text = user.first_name
            itemView.findViewById<TextView>(R.id.tvLastName).text = user.last_name
            itemView.findViewById<TextView>(R.id.tvEmail).text = user.email
            val imageViewAvatar: ImageView = itemView.findViewById(R.id.imgAvatar)
            Glide.with(itemView.context)
                .load(user.avatar)
                .into(imageViewAvatar)
        }
    }
}