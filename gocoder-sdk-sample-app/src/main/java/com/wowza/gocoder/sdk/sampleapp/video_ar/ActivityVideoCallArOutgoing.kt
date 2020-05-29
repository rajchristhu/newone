package com.wowza.gocoder.sdk.sampleapp.video_ar

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.wowza.gocoder.sdk.sampleapp.R
import kotlinx.android.synthetic.main.video_ar_outgoing_call_screen.*
import kotlinx.android.synthetic.main.video_outgoing_call_screen.camera


class ActivityVideoCallArOutgoing : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        window.sharedElementEnterTransition = ChangeBounds().setDuration(5000)
//        window.requestFeature(Window.FEATURE_CONTENT_TRANSITIONS)
//        window.enterTransition = Fade()
        setContentView(R.layout.video_ar_outgoing_call_screen)
        camera.start()
        val p1: Pair<View, String> = Pair(ar_out_going_caller_image, "caller_image")
        val p2: Pair<View, String> = Pair(ar_out_going_caller_name, "caller_name")
        val p3: Pair<View, String> = Pair(video_ar_icon, "video")
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,p1, p2, p3)
        end_call.setOnClickListener {
            startActivity(Intent(this@ActivityVideoCallArOutgoing, ActivityVideoCallArAccepted::class.java), options.toBundle())
//            startActivity( Intent(this@ActivityVideoCallArOutgoing, ActivityVideoCallArAccepted::class.java))
        }
    }
}
