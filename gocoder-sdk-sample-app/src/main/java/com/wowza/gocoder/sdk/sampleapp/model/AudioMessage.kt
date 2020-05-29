package com.wowza.gocoder.sdk.sampleapp.model

import java.util.*

data class AudioMessage (var audioPath: String,
                         override var position :Int,
                         override var time: Date,
                         override var timeStampe: Long,
                         override var senderId: String,
                         override var recipientId: String,
                         override var senderName: String,
                         override var channelId: String,
                         override var type: String = MessageType.AUDIO

)
    : Message {
    constructor() : this("",0, Date(0), 0L,"", "", "","")
}