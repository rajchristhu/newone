package com.wowza.gocoder.sdk.sampleapp.item

//import com.focuzar.holoassist.glide.GlideApp

import android.app.ActionBar
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.app.adprogressbarlib.AdCircleProgress
import com.wowza.gocoder.sdk.sampleapp.Chat.VideoActivity

import com.wowza.gocoder.sdk.sampleapp.model.VideoMessage
import com.focuzar.holoassist.util.FirestoreUtil
import com.focuzar.holoassist.util.StorageUtil
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.wowza.gocoder.sdk.sampleapp.R
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_image_message.*
import org.jetbrains.anko.startActivity
import java.io.File
import java.util.*


class VideoMessageItem(
        val message: VideoMessage,
        val context: Context,
        val mReference: StorageReference?,
        val urisdd: Uri?,
        val currentChannelId: String?,
        val timestamp: Long?
) : MessageItem(message) {

    var flag: Boolean = false
    override fun bind(viewHolder: ViewHolder, position: Int) {
        super.bind(viewHolder, position)

        if (message.senderId.equals("fcffbc55e")) {
            val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.END;
            viewHolder.message_root.layoutParams = params
        } else {
            val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.START;
            viewHolder.message_root.layoutParams = params
        }

//        if (mReference != null) {
//            if (!flag) {
//                storages(viewHolder.pgb_progress)
//                val root = Environment.getExternalStorageDirectory().toString()
//                val myDir = File("$root/holoAsisist/images/sent/Image-${message.timeStampe}.mp4")
//                GlideApp.with(context)
//                    .load(myDir.absoluteFile)
//                    .placeholder(R.drawable.ic_image_black_24dp)
//                    .into(viewHolder.imageView_message_image)
//                viewHolder.imageView_message_image.setOnClickListener {
//                    context!!.startActivity<VideoActivity>("uri" to "$root/holoAsisist/images/received/Image-${message.timeStampe}.mp4")
//                }
//            }
//            else {
//                val root = Environment.getExternalStorageDirectory().toString()
//                val myDir = File("$root/holoAsisist/images/sent/Image-${message.timeStampe}.mp4")
//                GlideApp.with(context)
//                    .load(myDir.absoluteFile)
//                    .placeholder(R.drawable.ic_image_black_24dp)
//                    .into(viewHolder.imageView_message_image)
//                viewHolder.imageView_message_image.setOnClickListener {
//                    context!!.startActivity<VideoActivity>("uri" to "$root/holoAsisist/images/received/Image-${message.timeStampe}.mp4")
//                }
//            }
//        } else {
//            if (message.senderId.equals("fcffbc55e")) {
//                val root = Environment.getExternalStorageDirectory().toString()
//                val myDir = File("$root/holoAsisist/images/sent/Image-${message.timeStampe}.mp4")
//                GlideApp.with(context)
//                    .load(myDir.absoluteFile)
//                    .placeholder(R.drawable.ic_image_black_24dp)
//                    .into(viewHolder.imageView_message_image)
//                viewHolder.imageView_message_image.setOnClickListener {
//                    context!!.startActivity<VideoActivity>("uri" to "$root/holoAsisist/images/received/Image-${message.timeStampe}.mp4")
//                }
//            } else {
//                if (StorageUtil.isFileExist(message.timeStampe, "received", "mp4")) {
//                    val root = Environment.getExternalStorageDirectory().toString()
//                    val myDir = File("$root/holoAsisist/images/sent/Image-${message.timeStampe}.mp4")
//                    GlideApp.with(context)
//                        .load(myDir.absoluteFile)
//                        .placeholder(R.drawable.ic_image_black_24dp)
//                        .into(viewHolder.imageView_message_image)
//                    viewHolder.imageView_message_image.setOnClickListener {
//                        context!!.startActivity<VideoActivity>("uri" to "$root/holoAsisist/images/received/Image-${message.timeStampe}.mp4")
//                    }
//                } else {
//                    viewHolder.pgb_progress.visibility = View.VISIBLE
//                    StorageUtil.downloadVideoFile(context,
//                        message.videoPath,
//                        message.timeStampe,
//                        viewHolder.pgb_progress,
//                        viewHolder.imageView_message_image,
//                        "mp4"
//                    )
//                }
//            }
//        }
    }

    override fun getLayout() = R.layout.item_image_message

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is VideoMessageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? VideoMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

    fun storages(pgbProgress: AdCircleProgress) {
        try {
            flag = true
            pgbProgress.visibility = View.VISIBLE
            mReference?.putFile(urisdd!!)?.addOnCompleteListener { task ->
            }?.addOnProgressListener {
                val progressValue: Double = (100.0 * it.bytesTransferred) / it.totalByteCount;
                pgbProgress.setAdProgress(progressValue.toInt())
            }?.addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                val result = taskSnapshot!!.metadata!!.reference!!.downloadUrl
                result.addOnSuccessListener {
                    //                    progress!!.dismiss()
                    pgbProgress.visibility = View.GONE
                    val s = it.toString()
                    Log.e("ADffsd", s.toString())
                    pgbProgress.setAdProgress(100)
                    val messageToSend =
                        VideoMessage(
                            s, 0, Calendar.getInstance().time, timestamp!!,
                            "fcffbc55e",
                            "08d3820b2", "Guest", currentChannelId!!
                        )
                    FirestoreUtil.sendMessage(messageToSend, currentChannelId)


//                    download()
//                    postJob(dis, loca, qus1, qus2, type, credits, creditPoint, uri, namess, s)
                }
            }
        } catch (e: Exception) {
//            progress!!.dismiss()
        }
    }
}