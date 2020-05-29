package com.wowza.gocoder.sdk.sampleapp.Chat

import com.wowza.gocoder.sdk.sampleapp.model.AudioMessage
import com.wowza.gocoder.sdk.sampleapp.model.ImageMessage
import com.wowza.gocoder.sdk.sampleapp.model.TextMessage
import com.wowza.gocoder.sdk.sampleapp.model.VideoMessage


interface ChatListner {
    fun chatMessage(messageItem: TextMessage)
    fun chatMessage(messageItem: ImageMessage)
    fun chatMessage(messageItem: VideoMessage)
    fun chatMessage(messageItem: AudioMessage)
}