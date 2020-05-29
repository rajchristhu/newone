package com.wowza.gocoder.sdk.sampleapp.audio_ar

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.wowza.gocoder.sdk.sampleapp.video_ar.ActivityVideoCallArAccepted
import com.wowza.gocoder.sdk.sampleapp.R
import kotlinx.android.synthetic.main.audio_ar_accepted_call_screen.*
import kotlinx.android.synthetic.main.video_outgoing_call_screen.camera

class ActivityAudioCallArAccepted : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.audio_ar_accepted_call_screen)
        camera.start()
        layout_btn_one_capture_btn.setOnClickListener {
            layout_btn_one.visibility = View.GONE
            layout_btn_two.visibility = View.VISIBLE
            layout_btn_one.visibility = View.GONE
            layout_btn_one.visibility = View.GONE
        }
        layout_btn_two_send_img_btn.setOnClickListener {
            layout_btn_one.visibility = View.GONE
            layout_btn_two.visibility = View.GONE
            layout_btn_three.visibility = View.VISIBLE
            layout_btn_four.visibility = View.GONE
        }

        layout_btn_three_send_ar_btn.setOnClickListener {
            layout_btn_one.visibility = View.GONE
            layout_btn_two.visibility = View.GONE
            layout_btn_three.visibility = View.GONE
            layout_btn_four.visibility = View.VISIBLE
        }
        layout_btn_foure_end_btn.setOnClickListener {
            startActivity(Intent(this@ActivityAudioCallArAccepted, ActivityVideoCallArAccepted::class.java))
        }
    }
}
