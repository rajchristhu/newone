package com.wowza.gocoder.sdk.sampleapp.Chat

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.focuzar.holoassist.utilities.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import com.wowza.gocoder.sdk.sampleapp.Chat.adapter.ChatCallAdapter
import com.wowza.gocoder.sdk.sampleapp.Chat.adapter.ChatMainAdapter
import com.wowza.gocoder.sdk.sampleapp.R
import kotlinx.android.synthetic.main.activity_chat.*

class ChatActivity : AppCompatActivity() {
    var db: FirebaseFirestore? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)
        setupDynamicDrawables()
        db = FirebaseFirestore.getInstance()
        db!!.collection("chatChannels").whereArrayContains("userIds", SessionMaintainence!!.instance!!.Uid.toString())
                .get()
                .addOnSuccessListener {
                    if (it.documents.size != 0) {
                        for (i in it) {

                        }
                    } else {

                    }

                }
                .addOnFailureListener { exception ->
                    Log.d("post", "get failed with ", exception)
                }
    }

    private fun setupDynamicDrawables() {
        val drawable = ContextCompat.getDrawable(applicationContext, R.drawable.chatwhite)
        button_left.drawable = drawable
        chat_recy.visibility = View.VISIBLE
        chat_recy.layoutManager = LinearLayoutManager(this)
        chat_recy.adapter = ChatMainAdapter(this)
        textView4.text = SessionMaintainence.instance!!.Uid


        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(s: CharSequence, start: Int,
                                           count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                if (s.toString().trim().isEmpty()) {
                    val drawabless = ContextCompat.getDrawable(applicationContext, R.drawable.smallbuttonback)
                    button.background = drawabless
                    button.setTextColor(resources.getColor(R.color.textColor))
                } else {
                    val drawabless = ContextCompat.getDrawable(applicationContext, R.drawable.smallbuttonback)
                    button.background = drawabless
                    button.setTextColor(resources.getColor(R.color.textColors))
                }
            }
        })

        buttonGroup_dynamic.setOnPositionChangedListener { position ->
            if (position == 0) {
                val drawables = ContextCompat.getDrawable(applicationContext, R.drawable.chatwhite)
                button_left.drawable = drawables
                val drawable2ss = ContextCompat.getDrawable(applicationContext, R.drawable.callblcak)
                button_right.drawable = drawable2ss
                call_recy.visibility = View.GONE
                chat_recy.visibility = View.VISIBLE
                chat_recy.layoutManager = LinearLayoutManager(this)
                chat_recy.adapter = ChatMainAdapter(this)
//                drawable.setCount(drawable.getCount() + 1)
            } else {
                val drawabless = ContextCompat.getDrawable(applicationContext, R.drawable.chatblack)
                button_left.drawable = drawabless
                val drawable2s = ContextCompat.getDrawable(applicationContext, R.drawable.callwhite)
                button_right.drawable = drawable2s
                chat_recy.visibility = View.GONE
                call_recy.visibility = View.VISIBLE
                call_recy.layoutManager = LinearLayoutManager(this)
                call_recy.adapter = ChatCallAdapter(this)
            }
        }

        val drawable2 = ContextCompat.getDrawable(applicationContext, R.drawable.callblcak)
        button_right.drawable = drawable2

    }
}
