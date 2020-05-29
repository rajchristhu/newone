package com.focuzar.holoassist.item

import android.app.ActionBar
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.focuzar.holoassist.utilities.SessionMaintainence
import com.focuzar.holoassist.utilities.Utils
import com.wowza.gocoder.sdk.sampleapp.R
import com.wowza.gocoder.sdk.sampleapp.item.MessageItem
import com.wowza.gocoder.sdk.sampleapp.model.TextMessage
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.item_text_message.*


class TextMessageItem(val message: TextMessage, val dateFlage: Boolean,
                      val context: Context)
    : MessageItem(message) {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        super.bind(viewHolder, position)

        if (dateFlage) {
            viewHolder.date.visibility = View.VISIBLE
            viewHolder.date.text = Utils().returnDate(message.timeStampe)
        } else {
            viewHolder.date.visibility = View.GONE
        }
        var sessionMaintainence = SessionMaintainence.instance
        if (message.senderId.equals("fcffbc55e")) {
            viewHolder.receiver_pic.visibility = View.GONE
            viewHolder.sender_pic.visibility = View.VISIBLE
            viewHolder.data.background = ContextCompat.getDrawable(context, R.drawable.rect_round_white);
            val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.END;
            viewHolder.message_root.layoutParams = params

//            if (message.statusTick==0){
//                viewHolder.tick.setImageResource(R.drawable.ic_tickone);
//            }else if (message.statusTick==1){
//                viewHolder.tick.setImageResource(R.drawable.ic_ticktwo);
//            }else{
//                viewHolder.tick.setImageResource(R.drawable.ic_ticktwo);
//            }
        } else {
            viewHolder.receiver_pic.visibility = View.VISIBLE
            viewHolder.sender_pic.visibility = View.GONE
            viewHolder.data.background = ContextCompat.getDrawable(context, R.drawable.rect_round_primary_color);
            val params: FrameLayout.LayoutParams = FrameLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.START;
            viewHolder.message_root.layoutParams = params
        }

        viewHolder.textView_message_text.text = message.text
    }

    override fun getLayout() = R.layout.item_text_message

    override fun isSameAs(other: com.xwray.groupie.Item<*>?): Boolean {
        if (other !is TextMessageItem)
            return false
        if (this.message != other.message)
            return false
        return true
    }

    override fun equals(other: Any?): Boolean {
        return isSameAs(other as? TextMessageItem)
    }

    override fun hashCode(): Int {
        var result = message.hashCode()
        result = 31 * result + context.hashCode()
        return result
    }

}