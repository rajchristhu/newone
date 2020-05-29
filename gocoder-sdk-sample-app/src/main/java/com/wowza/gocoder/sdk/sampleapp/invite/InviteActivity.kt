package com.wowza.gocoder.sdk.sampleapp.invite

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.wowza.gocoder.sdk.sampleapp.R
import com.wowza.gocoder.sdk.sampleapp.invite.adapter.InviteAdapter
import kotlinx.android.synthetic.main.invite_screen.*

class InviteActivity : AppCompatActivity() {

    private lateinit var inviteViewModel: InviteViewModel
    private lateinit var inviteAdapter: InviteAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.invite_screen)
        initViewModel()
    }

    private fun initViewModel() {
        inviteViewModel = InviteViewModel()
        inviteViewModel.inviteList()
        inviteViewModel.inviteListResponse.observe(this, Observer {
            setAdapter(it)
        })
    }

    private fun setAdapter(date: List<String>?) {
        inviteAdapter = InviteAdapter(this@InviteActivity)
        recycler_view_invite.adapter = inviteAdapter
    }
}
