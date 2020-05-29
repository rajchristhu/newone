package com.wowza.gocoder.sdk.sampleapp.utilities

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.Window

import androidx.localbroadcastmanager.content.LocalBroadcastManager


class BaseActivity : Activity() {
    private val keyboardLayoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val heightDiff = rootLayout!!.rootView.height - rootLayout!!.height
        val contentViewTop = window.findViewById<View>(Window.ID_ANDROID_CONTENT).getTop()

        val broadcastManager = LocalBroadcastManager.getInstance(this@BaseActivity)

        if (heightDiff <= contentViewTop) {
            onHideKeyboard()

            val intent = Intent("KeyboardWillHide")
            broadcastManager.sendBroadcast(intent)
        } else {
            val keyboardHeight = heightDiff - contentViewTop
            onShowKeyboard(keyboardHeight)

            val intent = Intent("KeyboardWillShow")
            intent.putExtra("KeyboardHeight", keyboardHeight)
            broadcastManager.sendBroadcast(intent)
        }
    }

    private var keyboardListenersAttached = false
    private var rootLayout: ViewGroup? = null

    protected fun onShowKeyboard(keyboardHeight: Int) {}
    protected fun onHideKeyboard() {}
//
//    protected fun attachKeyboardListeners() {
//        if (keyboardListenersAttached) {
//            return
//        }
//
//        rootLayout = findViewById<View>(R.id.rootview) as ViewGroup?
//        rootLayout!!.viewTreeObserver.addOnGlobalLayoutListener(keyboardLayoutListener)
//
//        keyboardListenersAttached = true
//    }

    override fun onDestroy() {
        super.onDestroy()

        if (keyboardListenersAttached) {
            rootLayout!!.viewTreeObserver.removeGlobalOnLayoutListener(keyboardLayoutListener)
        }
    }
}