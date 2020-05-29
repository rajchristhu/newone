package com.wowza.gocoder.sdk.sampleapp.Chat

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.widget.MediaController
import android.widget.VideoView
import com.wowza.gocoder.sdk.sampleapp.R

import kotlinx.android.synthetic.main.activity_video.*
import java.io.File

class VideoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video)
        val videoView = findViewById<VideoView>(R.id.videoView)
        //Creating MediaController
       val uri = intent.getStringExtra("uri")
        val myUri = Uri.parse(uri)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(videoView)
        //specify the location of media file
//        val uri: Uri = parse(Environment.getExternalStorageDirectory().getPath() + "/Movies/video.mp4")
        //Setting MediaController and URI, then starting the videoView
        videoView.setMediaController(mediaController)
//        videoView.setVideoPath(uri)
        videoView.setVideoURI(myUri)
        videoView.requestFocus()
        videoView.start()
        imageView17.setOnClickListener {
            finish()
        }
    }
}

