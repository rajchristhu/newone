package com.wowza.gocoder.sdk.sampleapp.invite.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wowza.gocoder.sdk.sampleapp.R
import kotlinx.android.synthetic.main.invite_adapter.view.*

class InviteAdapter(val context: Context) : RecyclerView.Adapter<InviteAdapter.MyView>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyView {
        val itemView =
                LayoutInflater.from(parent.context).inflate(R.layout.invite_adapter, parent, false)
        return MyView(itemView)
    }

    override fun getItemCount(): Int {
        return 1
    }

    override fun onBindViewHolder(holder: MyView, position: Int) {
        holder.name.text = "Maria Christhu Rajan"
        holder.rootView.setOnClickListener {

        }
    }


    inner class MyView(view: View) : RecyclerView.ViewHolder(view) {
        val name = view.name
        val rootView = view.rootView
    }
}