package com.wowza.gocoder.sdk.sampleapp.login

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProviders
import com.focuzar.holoassist.utilities.SessionMaintainence
import com.google.firebase.firestore.FirebaseFirestore
import com.wowza.gocoder.sdk.sampleapp.Chat.ChatActivity
import com.wowza.gocoder.sdk.sampleapp.R
import com.wowza.gocoder.sdk.sampleapp.login.LoginViewmodel.LoginViewModel
import com.wowza.gocoder.sdk.sampleapp.login.model.UserModel
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.toast

class SignUpActivity : AppCompatActivity() {
    private lateinit var viewModel: LoginViewModel
    var db: FirebaseFirestore? = null
    val instance = SessionMaintainence.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        db = FirebaseFirestore.getInstance()

        editText25.setTextColor(resources.getColor(R.color.textColors))
        progressView.setTotal(6)
        button36.setOnClickListener {
            startActivity<ChatActivity>()
        }



        editText2.addTextChangedListener(object : TextWatcher {

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

                if (s.toString().isNotEmpty()) {
                    val drawabless =
                            ContextCompat.getDrawable(applicationContext, R.drawable.beforecon)
                    button3.background = drawabless
                    button3.setTextColor(resources.getColor(R.color.hintColor))
                } else {
                    val drawabless =
                            ContextCompat.getDrawable(applicationContext, R.drawable.smallbuttonback)
                    button3.background = drawabless
                    button3.setTextColor(resources.getColor(R.color.textColors))
                }
            }
        })

        button3.setOnClickListener {
            instance!!.firstn = editText2.text.toString()
            root1.visibility = View.GONE
            progressView.setProgress(2)
            imageView6.visibility = View.GONE
            root3.visibility = View.VISIBLE
        }



        editText22.addTextChangedListener(object : TextWatcher {

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

                if (s.toString().isNotEmpty()) {
                    val drawabless =
                            ContextCompat.getDrawable(applicationContext, R.drawable.beforecon)
                    button32.background = drawabless
                    button32.setTextColor(resources.getColor(R.color.hintColor))
                } else {
                    val drawabless =
                            ContextCompat.getDrawable(applicationContext, R.drawable.smallbuttonback)
                    button32.background = drawabless
                    button32.setTextColor(resources.getColor(R.color.textColors))
                }
            }
        })

        button32.setOnClickListener {
            if (editText22.text.toString().isNotEmpty()) {
                root3.visibility = View.GONE
                imageView6.visibility = View.GONE
                progressView.setProgress(3)
                SessionMaintainence.instance!!.lastname = editText22.text.toString()
                root4.visibility = View.VISIBLE
            } else {
                toast("please fill the field")
            }

        }




        editText24.addTextChangedListener(object : TextWatcher {

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

                if (s.toString().isNotEmpty()) {
                    val drawabless =
                            ContextCompat.getDrawable(applicationContext, R.drawable.beforecon)
                    button34.background = drawabless
                    button34.setTextColor(resources.getColor(R.color.hintColor))
                } else {
                    val drawabless =
                            ContextCompat.getDrawable(applicationContext, R.drawable.smallbuttonback)
                    button34.background = drawabless
                    button34.setTextColor(resources.getColor(R.color.textColors))
                }
            }
        })


        button34.setOnClickListener {
            if (editText24.text.toString().trim().isNotEmpty()) {
                progressView.setProgress(4)
                root4.visibility = View.GONE
                SessionMaintainence.instance!!.emailname = editText24.text.toString().trim()
                viewModel.doLogin(editText24.text.toString().trim())
                root5.visibility = View.VISIBLE
                imageView6.visibility = View.VISIBLE
            }
        }

        button35.setOnClickListener {
            if (editText25.value == SessionMaintainence.instance!!.pin) {
                root5.visibility = View.GONE
                root2.visibility = View.VISIBLE
                progressView.setProgress(5)
                imageView6.visibility = View.VISIBLE
            } else {
                toast("enter correct otp")
            }

        }


        editText21.addTextChangedListener(object : TextWatcher {

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

                if (s.toString().isNotEmpty()) {
                    editText3.addTextChangedListener(object : TextWatcher {

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

                            if (s.toString().isNotEmpty()) {

                                val drawabless =
                                        ContextCompat.getDrawable(applicationContext, R.drawable.beforecon)
                                button31.background = drawabless
                                button31.setTextColor(resources.getColor(R.color.hintColor))
                            } else {
                                val drawabless =
                                        ContextCompat.getDrawable(applicationContext, R.drawable.smallbuttonback)
                                button31.background = drawabless
                                button31.setTextColor(resources.getColor(R.color.textColors))
                            }
                        }
                    })


                } else {
                    val drawabless =
                            ContextCompat.getDrawable(applicationContext, R.drawable.smallbuttonback)
                    button31.background = drawabless
                    button31.setTextColor(resources.getColor(R.color.textColors))
                }
            }
        })


        button31.setOnClickListener {
            if (editText21.text.toString().trim() == editText3.text.toString().trim()) {
                instance!!.passw=editText21.text.toString().trim()
                login()
            }

        }
        imageView2.setOnClickListener {
            when {
                root1.visibility == View.VISIBLE -> finish()
                root3.visibility == View.VISIBLE -> {
                    progressView.setProgress(1)
                    root1.visibility = View.VISIBLE
                    imageView6.visibility = View.GONE
                    root3.visibility = View.GONE
                }
                root4.visibility == View.VISIBLE -> {
                    root3.visibility = View.VISIBLE
                    progressView.setProgress(2)
                    imageView6.visibility = View.GONE
                    root4.visibility = View.GONE
                }
                root5.visibility == View.VISIBLE -> {
                    progressView.setProgress(3)
                    root4.visibility = View.VISIBLE
                    root5.visibility = View.GONE
                    imageView6.visibility = View.GONE
                }
                root2.visibility == View.VISIBLE -> {
                    progressView.setProgress(4)

                    root5.visibility = View.VISIBLE
                    root2.visibility = View.GONE
                    imageView6.visibility = View.VISIBLE
                }
                root6.visibility == View.VISIBLE -> {
                    progressView.setProgress(5)

                    root2.visibility = View.VISIBLE
                    imageView6.visibility = View.VISIBLE
                    root6.visibility = View.GONE
                }
            }
        }
    }

    private fun login() {
        val docRef = db!!.collection("user").document(instance!!.emailname!!)

        docRef.get()
                .addOnSuccessListener { document ->
                        if (document.exists()) {
                            Log.d("post", "DocumentSnapshot data: ${document.data}")
                        } else {
                            Log.d("post", "No such document")
                            newuser()
                        }

                }
                .addOnFailureListener { exception ->
                    Log.d("post", "get failed with ", exception)
                }
    }

    private fun newuser() {
        val user = UserModel(instance!!.firstn!!,instance!!.lastname!!,instance!!.emailname!!,instance.passw!!,instance!!.emailname!!.take(9))
        db!!.collection("user").document(instance!!.emailname!!).set(user)
                .addOnSuccessListener {
                    root2.visibility = View.GONE
                    progressView.setProgress(6)
                    SessionMaintainence.instance!!.passw = editText3.text.toString().trim()
                    instance!!.is_loggedin=true
                    instance!!.Uid= instance!!.emailname!!.take(9)
                    imageView2.visibility = View.INVISIBLE
                    imageView6.visibility = View.GONE
                    root6.visibility = View.VISIBLE
                }
                .addOnFailureListener {
                    Log.d("post", "failed")

                }
    }

    override fun onBackPressed() {
        when {
            root1.visibility == View.VISIBLE -> finish()
            root3.visibility == View.VISIBLE -> {
                progressView.setProgress(1)
                root1.visibility = View.VISIBLE
                imageView6.visibility = View.GONE
                root3.visibility = View.GONE
            }
            root4.visibility == View.VISIBLE -> {
                root3.visibility = View.VISIBLE
                progressView.setProgress(2)
                imageView6.visibility = View.GONE
                root4.visibility = View.GONE
            }
            root5.visibility == View.VISIBLE -> {
                progressView.setProgress(3)
                root4.visibility = View.VISIBLE
                root5.visibility = View.GONE
                imageView6.visibility = View.GONE
            }
            root2.visibility == View.VISIBLE -> {
                progressView.setProgress(4)
                root5.visibility = View.VISIBLE
                root2.visibility = View.GONE
                imageView6.visibility = View.VISIBLE
            }
            root6.visibility == View.VISIBLE -> {
                progressView.setProgress(5)
                root2.visibility = View.VISIBLE
                imageView6.visibility = View.VISIBLE
                root6.visibility = View.GONE
            }
        }
    }
}
