package com.wowza.gocoder.sdk.sampleapp.login

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings.Secure
import android.telephony.TelephonyManager
import android.text.Editable
import android.text.TextWatcher
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.wowza.gocoder.sdk.sampleapp.Chat.ChatPageActivity
import com.focuzar.holoassist.utilities.SessionMaintainence
import com.wowza.gocoder.sdk.sampleapp.R
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.startActivity


class LoginActivity : AppCompatActivity() {
    var telephonyManager: TelephonyManager? = null
    val ss = ""

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var sessionMaintainence = SessionMaintainence.instance
//        sessionMaintainence!!.is_loggedin = true

        sessionMaintainence!!.firstName = getDeviceUniqueID(this).take(9)
        textView4.text = sessionMaintainence!!.firstName
//        view.setOnLongClickListener {
//            it.tooltipText = "Copied"
////            TooltipCompat.setTooltipText(it, this.getString(R.string.copied))
//            return@setOnLongClickListener true
//
//        }
        view.setOnClickListener {
            //Intent to share the text
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type="text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT,  "X-Assist joining code, "+sessionMaintainence!!.firstName);
            startActivity(Intent.createChooser(shareIntent,"Share via"))
        }
        editText.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {

//                if (s.length==3||s.length==7||s.length==11)
//                {
//                    s.append(" ")
//                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

                if (s.toString().trim().length != 9) {
                    val drawabless =
                        ContextCompat.getDrawable(applicationContext, R.drawable.beforecon)
                    button.background = drawabless
                    button.setTextColor(resources.getColor(R.color.hintColor))
                } else {
                    val drawabless =
                        ContextCompat.getDrawable(applicationContext, R.drawable.smallbuttonback)
                    button.background = drawabless
                    button.setTextColor(resources.getColor(R.color.textColors))
                }
            }
        })
        button2.setOnClickListener {
            startActivity<SignInActivity>()
        }
        button.setOnClickListener {
            if (editText.text.toString().trim().length == 9) {

                sessionMaintainence!!.fullname = editText.text.toString().trim()
                startActivity<ChatPageActivity>()
            }

        }
        textView6.setOnClickListener {
            startActivity<SignUpActivity>()
        }
    }

    @SuppressLint("HardwareIds")
    fun getDeviceUniqueID(activity: Activity): String {
        return Secure.getString(
            activity.contentResolver,
            Secure.ANDROID_ID
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}
