package com.wowza.gocoder.sdk.sampleapp.intro

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wowza.gocoder.sdk.sampleapp.login.LoginActivity
import com.focuzar.holoassist.utilities.SessionMaintainence
import com.wowza.gocoder.sdk.sampleapp.Chat.ChatActivity
import com.wowza.gocoder.sdk.sampleapp.MainActivity
import com.wowza.gocoder.sdk.sampleapp.R

import org.jetbrains.anko.startActivity

class checkActivity : AppCompatActivity() {
    var sessionMaintainence = SessionMaintainence.instance
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_check)
        val login = sessionMaintainence!!.is_loggedin
        if (!login) {
            startActivity<LoginActivity>()
            finish()
        } else {
            startActivity<ChatActivity>()
            finish()

        }
    }
}
