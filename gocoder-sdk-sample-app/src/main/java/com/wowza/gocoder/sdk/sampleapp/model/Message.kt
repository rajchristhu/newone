package com.wowza.gocoder.sdk.sampleapp.model

import java.util.*


object MessageType {
    const val TEXT = "TEXT"
    const val IMAGE = "IMAGE"
    const val AUDIO = "AUDIO"
    const val VIDEO = "VIDEO"
}

object MessageStatus{
    const val PENDING = "PENDING"
    const val COMPLETED = "COMPLETED"
}

interface Message {
    val position: Int
    val time: Date
    val timeStampe: Long
    val senderId: String
    val recipientId: String
    val senderName: String
    val channelId: String
    val type: String
}