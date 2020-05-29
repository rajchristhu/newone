package com.focuzar.holoassist.roomdb

import androidx.room.*
import java.sql.Timestamp

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createUser(message: MessageUser)

    @Query("SELECT * FROM MessageUser where senderId = :senderId AND recipientId = :receiverId ORDER BY time_stamp ASC")
    fun findAll(senderId: String, receiverId: String): List<MessageUser>

    @Query("SELECT * FROM MessageUser where senderId = :senderId AND recipientId = :receiverId AND time_stamp=:timestamp AND type=:type ORDER BY time_stamp ASC")
    fun find(senderId: String, receiverId: String, timestamp: Long, type: String): List<MessageUser>

    @Update
    fun updateMessage(message: MessageUser)

    @Query("UPDATE MessageUser SET status = :status where channelId = :channel AND senderId= :senderId AND recipientId = :receiverId AND time_stamp = :timestamp")
    fun updateMessageData(status: String, channel: String, senderId: String, receiverId: String, timestamp: Long)

    @Query("UPDATE MessageUser SET status = :status, imagePath = :imagePath where channelId = :channel AND senderId = :senderId AND recipientId = :receiverId AND time_stamp = :timestamp")
    fun updateFileMessageData(status: String, imagePath: String, channel: String, senderId: String, receiverId: String, timestamp: Long)

    @Delete
    fun deleteUser(message: MessageUser)
}