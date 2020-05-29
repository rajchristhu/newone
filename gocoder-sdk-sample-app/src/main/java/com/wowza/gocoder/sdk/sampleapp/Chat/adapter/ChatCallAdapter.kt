package com.wowza.gocoder.sdk.sampleapp.Chat.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wowza.gocoder.sdk.sampleapp.Chat.ChatActivity
import com.wowza.gocoder.sdk.sampleapp.Chat.ChatPageActivity
import com.wowza.gocoder.sdk.sampleapp.R
import kotlinx.android.synthetic.main.call_main_adapter.view.*
import org.jetbrains.anko.startActivity

class ChatCallAdapter(val chatActivity: ChatActivity) :
    RecyclerView.Adapter<ChatCallAdapter.MyView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.call_main_adapter, parent, false)
        return MyView(itemView)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        holder.name.text="Maria Christhu Rajan"
        holder.rootchat.setOnClickListener {
            chatActivity.startActivity<ChatPageActivity>()
        }
    }


    inner class MyView(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.name
        val rootchat = view.rootchat
    }
}