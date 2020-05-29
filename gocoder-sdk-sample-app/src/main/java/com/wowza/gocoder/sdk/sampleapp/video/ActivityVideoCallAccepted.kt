package com.focuzar.holoassist.video

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wowza.gocoder.sdk.sampleapp.R
import kotlinx.android.synthetic.main.video_outgoing_call_screen.*

class ActivityVideoCallAccepted : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.video_accepted_call_screen)
        camera.start()
    }
}
