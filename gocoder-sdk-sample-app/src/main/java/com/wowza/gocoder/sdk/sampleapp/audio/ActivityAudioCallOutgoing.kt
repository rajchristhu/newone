package com.wowza.gocoder.sdk.sampleapp.audio

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wowza.gocoder.sdk.sampleapp.R
import kotlinx.android.synthetic.main.audio_call_outgoing_screen.*

class ActivityAudioCallOutgoing : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_call_outgoing_screen)
        endcall.setOnClickListener {
            startActivity(Intent(this@ActivityAudioCallOutgoing, ActivityAudioCallAccepted::class.java))
        }
    }
}
