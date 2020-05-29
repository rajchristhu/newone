package com.wowza.gocoder.sdk.sampleapp.item

import android.app.ActionBar
import android.content.Context
import android.media.MediaPlayer
import android.os.Handler
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import com.wowza.gocoder.sdk.sampleapp.R
import com.wowza.gocoder.sdk.sampleapp.model.AudioMessage

import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_audio_message.*


class AudioMessageItem(val message: AudioMessage,
                       val context: Context)
    : MessageItem(message), View.OnClickListener, View.OnTouchListener,


        MediaPlayer.OnCompletionListener, MediaPlayer.OnBufferingUpdateListener {
    override fun onClick(p0: View?) {
        if (p0!!.id == R.id.play) {
            if (message.audioPath != "") {
                try {
                    mediaPlayer!!.setDataSource(message.audioPath)
                    mediaPlayer!!.prepare()
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                mediaFileLengthInMilliseconds =
                        mediaPlayer!!.duration // gets the song length in milliseconds from URL

                if (!mediaPlayer!!.isPlaying) {
                    mediaPlayer!!.start()
                    buttonPlayPause!!.setImageResource(0)
                    buttonPlayPause!!.setImageResource(R.drawable.ic_send_black_24dp)
                } else {
                    mediaPlayer!!.pause()
                    buttonPlayPause!!.setImageResource(R.drawable.mic_ic)
                }

                primarySeekBarProgressUpdater()
            }
//            else {
//                seekBarProgress!!.isEnabled = false
//            }
        }
    }

    override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
        if (p0!!.id == R.id.seekkBar) {
            /** Seekbar onTouch event handler. Method which seeks MediaPlayer to seekBar primary progress position */
            if (mediaPlayer!!.isPlaying) {
                val sb = p0 as SeekBar
                val playPositionInMillisecconds = mediaFileLengthInMilliseconds / 100 * sb.progress
                mediaPlayer!!.seekTo(playPositionInMillisecconds)
            }
        }
        return false
    }

    override fun onCompletion(mp: MediaPlayer?) {
        buttonPlayPause!!.setImageResource(R.drawable.mic_ic)
    }

    override fun onBufferingUpdate(mp: MediaPlayer?, percent: Int) {
        seekBarProgress!!.secondaryProgress = percent
    }

    private var buttonPlayPause: ImageView? = null
    private var seekBarProgress: SeekBar? = null
    private var mediaFileLengthInMilliseconds: Int = 0
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private var pause: Boolean = false
    override fun bind(viewHolder: ViewHolder, position: Int) {
        super.bind(viewHolder, position)
        if (message.senderId.equals("fcffbc55e")) {
            val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;
            viewHolder.message_root.layoutParams = params
        } else {
            val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START;
            viewHolder.message_root.layoutParams = params
        }
        buttonPlayPause = viewHolder.play
        buttonPlayPause!!.setOnClickListener(this)

        seekBarProgress = viewHolder.seekkBar
        seekBarProgress!!.max = 99
        seekBarProgress!!.setOnTouchListener(this)

        mediaPlayer = MediaPlayer()
        mediaPlayer!!.setOnBufferingUpdateListener(this)
        mediaPlayer!!.setOnCompletionListener(this)
        if (message.audioPath == "") {
            seekBarProgress!!.isEnabled = false
        }
//        GlideApp.with(context)
//            .load(StorageUtil.pathToReference(message.imagePath))
//            .placeholder(R.drawable.ic_image_black_24dp)
//            .into(viewHolder.imageView_message_image)'''


    }

    override fun getLayout() = R.layout.item_audio_message

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is AudioMessageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? AudioMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

    private fun primarySeekBarProgressUpdater() {
        seekBarProgress!!.progress =
                (mediaPlayer!!.currentPosition.toFloat() / mediaFileLengthInMilliseconds * 100).toInt() // This math construction give a percentage of "was playing"/"song length"
        if (mediaPlayer!!.isPlaying) {
            val notification = Runnable { primarySeekBarProgressUpdater() }
            handler.postDelayed(notification, 1000)
        }
    }
}