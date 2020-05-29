package com.focuzar.holoassist.util

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.CursorLoader
import android.database.Cursor
import android.net.Uri
import android.os.AsyncTask
import android.provider.MediaStore
import android.util.Log
import com.focuzar.holoassist.item.TextMessageItem
import com.wowza.gocoder.sdk.sampleapp.Chat.ChatListner

import com.focuzar.holoassist.roomdb.MessageUser
import com.wowza.gocoder.sdk.sampleapp.utilities.Application
import com.focuzar.holoassist.utilities.SessionMaintainence
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.wowza.gocoder.sdk.sampleapp.item.*
import com.wowza.gocoder.sdk.sampleapp.model.*
import com.xwray.groupie.kotlinandroidextensions.Item
import java.util.*


object FirestoreUtil {
    private val firestoreInstance: FirebaseFirestore by lazy { FirebaseFirestore.getInstance() }
    var sessionMaintainence = SessionMaintainence.instance

    private val currentUserDocRef: DocumentReference
        get() = firestoreInstance.document(
            "users/${sessionMaintainence!!.firstName
                ?: throw NullPointerException("UID is null.")}"
        )

    private val chatChannelsCollectionRef = firestoreInstance.collection("chatChannels")

    fun initCurrentUserIfFirstTime(onComplete: () -> Unit) {
        currentUserDocRef.get().addOnSuccessListener { documentSnapshot ->
            if (!documentSnapshot.exists()) {
                val newUser = User(
                    FirebaseAuth.getInstance().currentUser?.displayName ?: "",
                    "", null, mutableListOf()
                )
                currentUserDocRef.set(newUser).addOnSuccessListener {
                    onComplete()
                }
            } else
                onComplete()
        }
    }

    fun updateCurrentUser(name: String = "", bio: String = "", profilePicturePath: String? = null) {
        val userFieldMap = mutableMapOf<String, Any>()
        if (name.isNotBlank()) userFieldMap["name"] = name
        if (bio.isNotBlank()) userFieldMap["bio"] = bio
        if (profilePicturePath != null)
            userFieldMap["profilePicturePath"] = profilePicturePath
        currentUserDocRef.update(userFieldMap)
    }

    fun getCurrentUser(onComplete: (User) -> Unit) {
        currentUserDocRef.get()
            .addOnSuccessListener {
                onComplete(it.toObject(User::class.java)!!)
            }
    }

    fun addUsersListener(context: Context, onListen: (List<Item>) -> Unit): ListenerRegistration {
        return firestoreInstance.collection("users")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "Users listener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item>()
                querySnapshot!!.documents.forEach {
                    if (it.id != sessionMaintainence!!.firstName)
                        items.add(PersonItem(it.toObject(User::class.java)!!, it.id, context))
                }
                onListen(items)
            }
    }

    fun removeListener(registration: ListenerRegistration) = registration.remove()

    fun getOrCreateChatChannel(
        otherUserId: String,
        onComplete: (channelId: String) -> Unit
    ) {
        currentUserDocRef.collection("engagedChatChannels")
            .document("08d3820b2").get().addOnSuccessListener {
                if (it.exists()) {
                    onComplete(it["channelId"] as String)
                    return@addOnSuccessListener
                }

                val currentUserId = sessionMaintainence!!.firstName

                val newChannel = chatChannelsCollectionRef.document()
                newChannel.set(ChatChannel(mutableListOf(currentUserId!!, "08d3820b2")))

                currentUserDocRef
                    .collection("engagedChatChannels")
                    .document("08d3820b2")
                    .set(mapOf("channelId" to newChannel.id))

                firestoreInstance.collection("users").document("08d3820b2")
                    .collection("engagedChatChannels")
                    .document(currentUserId)
                    .set(mapOf("channelId" to newChannel.id))

                onComplete(newChannel.id)
            }
    }

    fun addChatMessagesListener(
            chatListner: ChatListner,
            channelId: String, context: Activity,
            onListen: (MutableList<Item>) -> Unit
    ): ListenerRegistration {
        return chatChannelsCollectionRef.document(channelId).collection("messages")
            .orderBy("time")
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.e("FIRESTORE", "ChatMessagesListener error.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                val items = mutableListOf<Item>()
                querySnapshot!!.documents.forEach {

                    if (it["type"] == MessageType.TEXT) {
                        items.add(
                            TextMessageItem(
                                it.toObject(TextMessage::class.java)!!,true,
                                context
                            )
                        )
                        isTextMessageExist(it.toObject(TextMessage::class.java)!!, chatListner)
//                        updateData(it.toObject(TextMessage::class.java)!!)
                    } else if (it["type"] == MessageType.IMAGE) {
                        items.add(
                            ImageMessageItem(
                                it.toObject(ImageMessage::class.java)!!,
                                context,
                                null,
                                null,
                                null,
                                0,
                                null
                            )
                        )
                        isTextMessageExist(it.toObject(ImageMessage::class.java)!!, chatListner)
                    } else if (it["type"] == MessageType.AUDIO) {
                        items.add(
                            AudioMessageItem(
                                it.toObject(AudioMessage::class.java)!!,
                                context
                            )
                        )
                        isTextMessageExist(it.toObject(AudioMessage::class.java)!!, chatListner)
                    } else {
                        items.add(
                            VideoMessageItem(
                                it.toObject(VideoMessage::class.java)!!,
                                context,
                                null,
                                null,
                                null,
                                null
                            )
                        )
                        isTextMessageExist(it.toObject(VideoMessage::class.java)!!, chatListner)
                    }

                    return@forEach
                }
//                onListen(items)
            }
    }

    fun sendMessage(message: Message, channelId: String) {
        chatChannelsCollectionRef.document(channelId)
            .collection("messages")
            .add(message)
    }

    //region FCM
    fun getFCMRegistrationTokens(onComplete: (tokens: MutableList<String>) -> Unit) {
        currentUserDocRef.get().addOnSuccessListener {
            val user = it.toObject(User::class.java)!!
            onComplete(user.registrationTokens)
        }
    }

    fun setFCMRegistrationTokens(registrationTokens: MutableList<String>) {
        currentUserDocRef.update(mapOf("registrationTokens" to registrationTokens))
    }

    //endregion FCM
    @SuppressLint("StaticFieldLeak")
    fun updateData(message: TextMessage) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                val dao = Application.database.messageDao()
                val message = MessageUser(
                    message.timeStampe,
                    message.text,
                    "",
                    "",
                    "",
                    "", "",
                    message.time.toString(),
                    message.channelId,
                    message.senderId,
                    message.recipientId,
                    message.senderName,
                    message.type,
                    MessageStatus.COMPLETED
                )
                dao.updateMessageData(
                    message.status,
                    message.channelId,
                    message.senderId,
                    message.recipientId,
                    message.time_stamp
                )
                return null
            }
        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun updateDataImage(message: ImageMessage) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                val dao = Application.database.messageDao()
                val message = MessageUser(
                    message.timeStampe,
                    "",
                    message.imagePath,
                    "",
                    "",
                    message.senderLocalPath,
                    "",
                    message.time.toString(),
                    message.channelId,
                    message.senderId,
                    message.recipientId,
                    message.senderName,
                    message.type,
                    MessageStatus.COMPLETED
                )
                dao.updateFileMessageData(
                    message.status,
                    message.imagePath,
                    message.channelId,
                    message.senderId,
                    message.recipientId,
                    message.time_stamp
                )
                return null
            }
        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun updateDataVideo(message: VideoMessage) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                val dao = Application.database.messageDao()
                val message = MessageUser(
                    message.timeStampe,
                    "",
                    "",
                    message.videoPath,
                    "", "", "",
                    message.time.toString(),
                    message.channelId,
                    message.senderId,
                    message.recipientId,
                    message.senderName,
                    message.type,
                    MessageStatus.COMPLETED
                )
                dao.updateFileMessageData(
                    message.status,
                    message.videopath,
                    message.channelId,
                    message.senderId,
                    message.recipientId,
                    message.time_stamp
                )
                return null
            }
        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun updateDataAudio(message: AudioMessage) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                val dao = Application.database.messageDao()
                val message = MessageUser(
                    message.timeStampe,
                    "",
                    "",
                    "", "", "",
                    message.audioPath,
                    message.time.toString(),
                    message.channelId,
                    message.senderId,
                    message.recipientId,
                    message.senderName,
                    message.type,
                    MessageStatus.COMPLETED
                )
                dao.updateFileMessageData(
                    message.status,
                    message.audiopath,
                    message.channelId,
                    message.senderId,
                    message.recipientId,
                    message.time_stamp
                )
                return null
            }
        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun insert(textMessage: TextMessage, chatListner: ChatListner) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                val dao = Application.database.messageDao()
                val message = MessageUser(
                    textMessage.timeStampe,
                    textMessage.text,
                    "",
                    "",
                    "", "", "",
                    textMessage.time.toString(),
                    textMessage.channelId,
                    textMessage.senderId,
                    textMessage.recipientId,
                    textMessage.senderName,
                    textMessage.type,
                    MessageStatus.PENDING
                )
                dao.createUser(message)
                chatListner.chatMessage(textMessage)
                return null
            }
        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun insert(imageMessage: ImageMessage, chatListner: ChatListner, flage: Boolean) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                val dao = Application.database.messageDao()
                val message = MessageUser(
                    imageMessage.timeStampe,
                    "",
                    "",
                    "",
                    "", imageMessage.senderLocalPath, "",
                    imageMessage.time.toString(),
                    imageMessage.channelId,
                    imageMessage.senderId,
                    imageMessage.recipientId,
                    imageMessage.senderName,
                    imageMessage.type,
                    MessageStatus.PENDING
                )
                dao.createUser(message)
                if (flage)
                    chatListner.chatMessage(imageMessage)
                return null
            }
        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun insert(videoMessage: VideoMessage, chatListner: ChatListner, flage: Boolean) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                val dao = Application.database.messageDao()
                val message = MessageUser(
                    videoMessage.timeStampe,
                    "",
                    "",
                    "",
                    "", "", "",
                    videoMessage.time.toString(),
                    videoMessage.channelId,
                    videoMessage.senderId,
                    videoMessage.recipientId,
                    videoMessage.senderName,
                    videoMessage.type,
                    MessageStatus.PENDING
                )
                dao.createUser(message)
                if (flage)
                    chatListner.chatMessage(videoMessage)
                return null
            }
        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun insert(audioMessage: AudioMessage, chatListner: ChatListner) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                val dao = Application.database.messageDao()
                val message = MessageUser(
                    audioMessage.timeStampe,
                    "",
                    "",
                    "", "", "",
                    "",
                    audioMessage.time.toString(),
                    audioMessage.channelId,
                    audioMessage.senderId,
                    audioMessage.recipientId,
                    audioMessage.senderName,
                    audioMessage.type,
                    MessageStatus.PENDING
                )
                dao.createUser(message)
                chatListner.chatMessage(audioMessage)
                return null
            }
        }.execute()
    }

    fun getMessage(senderId: String, receiverId: String): List<MessageUser> {
        val dao = Application.database.messageDao()
        val users = dao.findAll(senderId, receiverId)

        if (users.isEmpty()) {
            val list: MutableList<MessageUser> = mutableListOf()
            return list
        }

        return users
    }

    @SuppressLint("StaticFieldLeak")
    fun isTextMessageExist(message: TextMessage, chatListner: ChatListner) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                val dao = Application.database.messageDao()
                val messages =
                    dao.find(
                        message.senderId,
                        message.recipientId,
                        message.timeStampe,
                        message.type
                    )
                if (messages.isEmpty()) {
                    insert(message, chatListner)
                } else
                    updateData(message)

                return null
            }

        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun isTextMessageExist(message: AudioMessage, chatListner: ChatListner) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                val dao = Application.database.messageDao()
                val messages =
                    dao.find(
                        message.senderId,
                        message.recipientId,
                        message.timeStampe,
                        message.type
                    )
                if (messages.isEmpty()) {
                    insert(message, chatListner)
                } else
                    updateDataAudio(message)
                return null
            }
        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun isTextMessageExist(message: ImageMessage, chatListner: ChatListner) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                val dao = Application.database.messageDao()
                val messages =
                    dao.find(
                        message.senderId,
                        message.recipientId,
                        message.timeStampe,
                        message.type
                    )
                if (messages.isEmpty()) {
                    insert(message, chatListner, true)
                } else
                    updateDataImage(message)

                return null
            }
        }.execute()
    }

    @SuppressLint("StaticFieldLeak")
    fun isTextMessageExist(message: VideoMessage, chatListner: ChatListner) {
        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                val dao = Application.database.messageDao()
                val messages =
                    dao.find(
                        message.senderId,
                        message.recipientId,
                        message.timeStampe,
                        message.type
                    )
                if (messages.isEmpty()) {
                    insert(message, chatListner, true)
                } else
                    updateDataVideo(message)
                return null
            }
        }.execute()
    }

    fun convertMessageUserToTextMessage(messageUser: MessageUser): TextMessage {
        val textMessage = TextMessage()
        textMessage.timeStampe = messageUser.time_stamp
        textMessage.time = Date(messageUser.time)
        textMessage.text = messageUser.text
        textMessage.channelId = messageUser.channelId
        textMessage.senderId = messageUser.senderId
        textMessage.recipientId = messageUser.recipientId
        textMessage.senderName = messageUser.senderName
        textMessage.type = messageUser.type
        return textMessage
    }

    fun convertMessageUserToImageMessage(messageUser: MessageUser): ImageMessage {
        val imageMessage = ImageMessage()
        imageMessage.timeStampe = messageUser.time_stamp
        imageMessage.time = Date(messageUser.time)
        imageMessage.imagePath = messageUser.imagePath
        imageMessage.senderLocalPath = messageUser.senderLocalPath
        imageMessage.receiverLocalPath = messageUser.receiverLocalPath
        imageMessage.channelId = messageUser.channelId
        imageMessage.senderId = messageUser.senderId
        imageMessage.recipientId = messageUser.recipientId
        imageMessage.senderName = messageUser.senderName
        imageMessage.type = messageUser.type
        return imageMessage
    }

    fun convertMessageUserToAudioMessage(messageUser: MessageUser): AudioMessage {
        val audioMessage = AudioMessage()
        audioMessage.timeStampe = messageUser.time_stamp
        audioMessage.time = Date(messageUser.time)
        audioMessage.audioPath = messageUser.audiopath
        audioMessage.channelId = messageUser.channelId
        audioMessage.senderId = messageUser.senderId
        audioMessage.recipientId = messageUser.recipientId
        audioMessage.senderName = messageUser.senderName
        audioMessage.type = messageUser.type
        return audioMessage
    }

    fun convertMessageUserToVideoMessage(messageUser: MessageUser): VideoMessage {
        val videoMessage = VideoMessage()
        videoMessage.timeStampe = messageUser.time_stamp
        videoMessage.time = Date(messageUser.time)
        videoMessage.videoPath = messageUser.imagePath
        videoMessage.channelId = messageUser.channelId
        videoMessage.senderId = messageUser.senderId
        videoMessage.recipientId = messageUser.recipientId
        videoMessage.senderName = messageUser.senderName
        videoMessage.type = messageUser.type
        return videoMessage
    }

    fun getRealPathFromURI(contentUri: Uri, context: Activity): String? {
        val proj = arrayOf(MediaStore.Images.Media.DATA)
        val loader = CursorLoader(context, contentUri, proj, null, null, null)
        val cursor: Cursor = loader.loadInBackground()
        val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val result = cursor.getString(column_index)
        cursor.close()
        return result
    }
}