package com.wowza.gocoder.sdk.sampleapp.audio_ar

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wowza.gocoder.sdk.sampleapp.R
import kotlinx.android.synthetic.main.video_ar_outgoing_call_screen.*
import kotlinx.android.synthetic.main.video_outgoing_call_screen.camera

class ActivityAudioCallArOutgoing : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_ar_outgoing_call_screen)
        camera.start()
        end_call.setOnClickListener {
            startActivity(Intent(this@ActivityAudioCallArOutgoing, ActivityAudioCallArAccepted::class.java))
        }
    }
}
