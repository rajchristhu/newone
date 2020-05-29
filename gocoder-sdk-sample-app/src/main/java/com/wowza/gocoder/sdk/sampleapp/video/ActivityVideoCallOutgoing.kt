package com.wowza.gocoder.sdk.sampleapp.video

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wowza.gocoder.sdk.sampleapp.R
import kotlinx.android.synthetic.main.video_outgoing_call_screen.*

class ActivityVideoCallOutgoing : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_outgoing_call_screen)
        camera.start()
    }
}
