package com.focuzar.holoassist.roomdb

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class MessageUser constructor(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "time_stamp") val time_stamp: Long,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "imagePath") val imagePath: String,
    @ColumnInfo(name = "videopath") val videopath: String,
    @ColumnInfo(name = "audiopath") val audiopath: String,
    @ColumnInfo(name = "senderLocalPath") val senderLocalPath: String,
    @ColumnInfo(name = "receiverLocalPath") val receiverLocalPath: String,
    @ColumnInfo(name = "time") val time: String,
    @ColumnInfo(name = "channelId") val channelId: String,
    @ColumnInfo(name = "senderId") val senderId: String,
    @ColumnInfo(name = "recipientId") val recipientId: String,
    @ColumnInfo(name = "senderName") val senderName: String,
    @ColumnInfo(name = "type") val type: String,
    @ColumnInfo(name = "status") val status: String
)
