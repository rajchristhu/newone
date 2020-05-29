package com.wowza.gocoder.sdk.sampleapp.login

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import com.focuzar.holoassist.utilities.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import com.wowza.gocoder.sdk.sampleapp.Chat.ChatActivity
import com.wowza.gocoder.sdk.sampleapp.R
import com.wowza.gocoder.sdk.sampleapp.login.LoginViewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SignInActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    var db: FirebaseFirestore? = null
    var instance = SessionMaintainence.instance
    var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        db = FirebaseFirestore.getInstance()

        progressView.setTotal(2)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        initViewmodel()
        button3.setOnClickListener {
            SessionMaintainence.instance!!.emailname = editText2.text.toString()
//            viewModel.doLogin("rajchristhu39@gmail.com")
            login()

        }
        imageView2.setOnClickListener {
            if (root1.visibility == View.GONE) {
                progressView.setProgress(1)
                root1.visibility = View.VISIBLE
                root2.visibility = View.GONE
            } else {
                finish()
            }
        }
        button31.setOnClickListener {
            SessionMaintainence.instance!!.passw = editText21.text.toString()
            if (SessionMaintainence.instance!!.passw == password) {
                instance!!.is_loggedin=true
                startActivity<ChatActivity>()
            } else {
                toast("password mismatch")

            }


        }
    }


    private fun login() {
        val docRef = db!!.collection("user").document(instance!!.emailname!!)

        docRef.get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        password = document["password"].toString()
                        progressView.setProgress(2)
                        instance!!.Uid= document["uid"].toString()

                        root1.visibility = View.GONE
                        root2.visibility = View.VISIBLE
                    } else {
                        toast("email id not present")
                    }

                }
                .addOnFailureListener { exception ->
                    Log.d("post", "get failed with ", exception)
                }
    }

    fun initViewmodel() {
        viewModel._loginResponse.observe(this, androidx.lifecycle.Observer {
            Log.e("dsf", it.toString())
        })
    }

    override fun onBackPressed() {
        if (root1.visibility == View.GONE) {
            progressView.setProgress(1)
            root1.visibility = View.VISIBLE
            root2.visibility = View.GONE
        } else {
            finish()
        }
    }
}
