package com.wowza.gocoder.sdk.sampleapp.item

import android.app.ActionBar
import android.app.Activity
import android.graphics.Bitmap
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.app.adprogressbarlib.AdCircleProgress
import com.wowza.gocoder.sdk.sampleapp.model.ImageMessage
import com.focuzar.holoassist.util.FirestoreUtil
import com.focuzar.holoassist.util.StorageUtil
import com.focuzar.holoassist.util.StorageUtil.isFileExist
import com.wowza.gocoder.sdk.sampleapp.R
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_image_message.*
import java.util.*


class ImageMessageItem(
        val message: ImageMessage,
        val context: Activity,
        val selectedImageBytes: ByteArray?,
        val currentChannelId: String?,
        val selectedImageBmp: Bitmap?,
        val messageSize: Int,
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


        if (selectedImageBytes != null) {
            if (!flag)
                sorage(viewHolder.pgb_progress, viewHolder.imageView_message_image)
            else
                viewHolder.imageView_message_image.setImageBitmap(selectedImageBmp)
            Log.d("Adapter called", "  called")
        } else {
//            viewHolder.pgb_progress.visibility = View.VISIBLE
            if (message.senderId.equals("fcffbc55e")) {
                if (StorageUtil.getImageFromPath(message.timeStampe, "sent") != null)
                    viewHolder.imageView_message_image.setImageBitmap(
                        StorageUtil.getImageFromPath(
                            message.timeStampe, "sent"
                        )
                    )
            } else {

                if (isFileExist(message.timeStampe,"received","jpg")){
                    viewHolder.imageView_message_image.setImageBitmap(
                        StorageUtil.getImageFromPath(
                            message.timeStampe, "received"
                        )
                    )
                }else{
                    viewHolder.pgb_progress.visibility = View.VISIBLE
                    loadImagefromURL(viewHolder.pgb_progress, viewHolder.imageView_message_image)
                }

            }
        }
    }

    fun loadImagefromURL(
        pgb_progress: AdCircleProgress,
        imageviewMessageImage: ImageView
    ) {
        StorageUtil.downloadFile(message.imagePath, message.timeStampe, pgb_progress, imageviewMessageImage, "jpg")
//        GlideApp.with(context)
//            .load(StorageUtil.pathToReference(message.imagePath))
//            .transition(DrawableTransitionOptions.withCrossFade())
//            .listener(object : RequestListener<Drawable> {
//                override fun onLoadFailed(
//                    e: GlideException?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    pgb_progress.visibility = View.GONE
//                    return false
//                }
//
//                override fun onResourceReady(
//                    resource: Drawable?,
//                    model: Any?,
//                    target: Target<Drawable>?,
//                    dataSource: DataSource?,
//                    isFirstResource: Boolean
//                ): Boolean {
//                    pgb_progress.setAdProgress(100)
//                    pgb_progress.visibility = View.GONE
//
//                    return false
//                }
//
//            }).into(imageviewMessageImage)
    }

    override fun getLayout() = R.layout.item_image_message

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is ImageMessageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? ImageMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

    fun sorage(
        pgbProgress: AdCircleProgress,
        imageviewMessageImage: ImageView
    ) {
        flag = true
        pgbProgress.visibility = View.VISIBLE
        imageviewMessageImage.setImageBitmap(selectedImageBmp)
        StorageUtil.uploadMessageImage(selectedImageBytes!!, pgbProgress) { imagePath ->
            val messageToSend =
                ImageMessage(
                    imagePath,
                    message.senderLocalPath,
                    "",
                    messageSize,
                    Calendar.getInstance().time,
                    timestamp!!,
                    "fcffbc55e",
                    "08d3820b2",
                    "Guest",
                    currentChannelId!!
                )
            FirestoreUtil.sendMessage(messageToSend, currentChannelId)
            FirestoreUtil.updateDataImage(messageToSend)
        }
    }
}