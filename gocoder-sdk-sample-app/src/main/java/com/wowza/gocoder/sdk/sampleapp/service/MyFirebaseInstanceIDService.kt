package com.wowza.gocoder.sdk.sampleapp.service

import com.google.firebase.auth.FirebaseAuth
import com.focuzar.holoassist.util.FirestoreUtil
import com.google.firebase.messaging.FirebaseMessagingService


class MyFirebaseInstanceIDService : FirebaseMessagingService() {

    override fun onNewToken (string: String) {
//        val newRegistrationToken = FirebaseInstanceId.getInstance().token

        if (FirebaseAuth.getInstance().currentUser != null)
            addTokenToFirestore(string)
    }

    companion object {
        fun addTokenToFirestore(newRegistrationToken: String?) {
            if (newRegistrationToken == null) throw NullPointerException("FCM token is null.")

            FirestoreUtil.getFCMRegistrationTokens { tokens ->
                if (tokens.contains(newRegistrationToken))
                    return@getFCMRegistrationTokens

                tokens.add(newRegistrationToken)
                FirestoreUtil.setFCMRegistrationTokens(tokens)
            }
        }
    }
}