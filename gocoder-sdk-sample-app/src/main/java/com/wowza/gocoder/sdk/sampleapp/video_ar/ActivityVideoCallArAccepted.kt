package com.wowza.gocoder.sdk.sampleapp.video_ar

import android.content.Intent
import android.os.Bundle
import android.transition.ChangeBounds
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.wowza.gocoder.sdk.sampleapp.R
import com.wowza.gocoder.sdk.sampleapp.audio.ActivityAudioCallOutgoing
import kotlinx.android.synthetic.main.video_outgoing_call_screen.*


class ActivityVideoCallArAccepted : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.sharedElementEnterTransition = ChangeBounds().setDuration(1000)
        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//        window.enterTransition = ChangeImageTransform()
        setContentView(R.layout.video_ar_accepted_call_screen)
        camera.start()

        endcall.setOnClickListener {
            startActivity(Intent(this@ActivityVideoCallArAccepted, ActivityAudioCallOutgoing::class.java))
        }
    }
    override fun onBackPressed() {
        ActivityCompat.finishAfterTransition(this)
    }
}
