package com.wowza.gocoder.sdk.sampleapp.Chat

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.devlomi.record_view.OnRecordListener
import com.devlomi.record_view.RecordButton
import com.devlomi.record_view.RecordView
import com.focuzar.holoassist.item.TextMessageItem
import com.wowza.gocoder.sdk.sampleapp.item.AudioMessageItem
import com.wowza.gocoder.sdk.sampleapp.item.ImageMessageItem
import com.wowza.gocoder.sdk.sampleapp.item.VideoMessageItem
import com.focuzar.holoassist.roomdb.MessageUser
import com.focuzar.holoassist.util.AudioUtil
import com.focuzar.holoassist.util.FirestoreUtil
import com.focuzar.holoassist.util.FirestoreUtil.insert
import com.focuzar.holoassist.util.StorageUtil
import com.focuzar.holoassist.utilities.SessionMaintainence
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.wowza.gocoder.sdk.sampleapp.R
import com.wowza.gocoder.sdk.sampleapp.graphics.OpenGLActivity
import com.wowza.gocoder.sdk.sampleapp.model.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions
import kotlinx.android.synthetic.main.activity_chat_page.*
import org.jetbrains.anko.startActivity
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.util.*

private const val RC_SELECT_IMAGE = 2
private const val RC_SELECT_VIDEO = 4


class ChatPageActivity : AppCompatActivity() , ChatListner {
    var sessionMaintainence = SessionMaintainence.instance
    private val PERMISSION_CODE = 1000;
    private val IMAGE_CAPTURE_CODE = 1001
    var image_uri: Uri? = null
    lateinit var uris: Uri
    lateinit var urisdd: Uri
    private lateinit var pathaa: String
    private lateinit var currentChannelId: String
    private lateinit var currentUser: User
    private lateinit var otherUserId: String
    private var mediaPlayer: MediaPlayer? = null
    private var mediaFileLengthInMilliseconds: Int = 0
    private lateinit var messagesListenerRegistration: ListenerRegistration
    private var shouldInitRecyclerView = true
    private lateinit var messagesSection: Section
    private var myAudioRecorder: MediaRecorder? = null
    lateinit var mStorage: StorageReference
    var firestoreDB: FirebaseFirestore? = null
    private var mRecorder: MediaRecorder? = null
    private var outputFile: String? = null
    var messageList: List<MessageUser> = arrayListOf()
    var message: MutableList<Item> = mutableListOf()
    val items = mutableListOf<Item>()
    internal var mPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_page)
        messagesSection = Section(items)
        mFileName = Environment.getExternalStorageDirectory().absolutePath
        mFileName += "/${Timestamp.now()}audio.mp3"
        imageView20.setOnClickListener {
            finish()
        }
        firestoreDB = FirebaseFirestore.getInstance()
        mStorage = FirebaseStorage.getInstance().getReference("Audio_Postjob")
        mediaPlayer = MediaPlayer()
        outputFile = Environment.getExternalStorageDirectory().absolutePath + "/recording.3gp"
        try {
            myAudioRecorder = MediaRecorder()
            myAudioRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
            myAudioRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            myAudioRecorder!!.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
            myAudioRecorder!!.setOutputFile(mFileName)
        } catch (e: Exception) {
            Log.e("FDfdf", e.toString())
        }
        AudioUtil.requestPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

        var emojIcon = EmojIconActions(this, rootview, editText_message, imageView19)
        emojIcon.ShowEmojIcon()
        emojIcon.setIconsIds(R.drawable.ic_action_keyboard, R.drawable.smiley)
        emojIcon.setKeyboardListener(object : EmojIconActions.KeyboardListener {
            override fun onKeyboardOpen() {
                Log.e("", "Keyboard opened!")
            }

            override fun onKeyboardClose() {
                Log.e("", "Keyboard closed")
            }
        })


        imageView16.setOnClickListener {
            //if sysem os is Marshmallow or Above, we need to request runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.CAMERA)
                    == PackageManager.PERMISSION_DENIED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED
                ) {
                    //permission was not enabled
                    val permission = arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    //show popup to request permission
                    requestPermissions(permission, PERMISSION_CODE)
                } else {
                    //permission already granted
                    openCamera()
                }
            } else {
                //system os is < marshmallow
                openCamera()
            }
        }
        imageView12.setOnClickListener {
            startActivity<OpenGLActivity>()
        }
//        FirestoreUtil.getCurrentUser {
//            currentUser = it
//        }
//        otherUserId = intent.getStringExtra(AppConstants.USER_ID)
        FirestoreUtil.getOrCreateChatChannel(sessionMaintainence!!.fullname!!) { channelId ->
            currentChannelId = channelId

//            messagesListenerRegistration =
//                FirestoreUtil.addChatMessagesListener(channelId, this, this::updateRecyclerView)

            loadUser(channelId)

            imageView_send.setOnClickListener {
                //                val intent =  Intent(Intent.ACTION_VIEW, Uri.parse(newVideoPath));
//intent.setDataAndType(Uri.parse(newVideoPath), "video/mp4");
//startActivity(intent);
                val intent = Intent().apply {
                    type = "video/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("video/mp4"))
                }
                startActivityForResult(
                    Intent.createChooser(intent, "Select Image"),
                    RC_SELECT_VIDEO
                )

            }
            imageView21.setOnClickListener {
                //                val messageToSend =
//                    TextMessage(editText_message.text.toString(), Calendar.getInstance().time,
//                        FirebaseAuth.getInstance().currentUser!!.uid,
//                        otherUserId, currentUser.name)
                val date = Calendar.getInstance().time
                val timestamp = Calendar.getInstance().timeInMillis
                val senderId = "fcffbc55e"
                val recevierId = "08d3820b2"
                val senderName = "Guest"
                val messageToSend =

                    TextMessage(
                        editText_message.text.toString(), message.size, date, timestamp,
                        senderId,
                        recevierId, senderName, channelId
                    )
                editText_message.setText("")
                insert(messageToSend, this@ChatPageActivity)
                FirestoreUtil.sendMessage(messageToSend, channelId)
            }

            fab_send_image.setOnClickListener {
                val intent = Intent().apply {
                    type = "image/*"
                    action = Intent.ACTION_GET_CONTENT
                    putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/jpeg", "image/png"))
                }
                startActivityForResult(
                    Intent.createChooser(intent, "Select Image"),
                    RC_SELECT_IMAGE
                )
            }
        }
        val recordView = findViewById<View>(R.id.record_view) as RecordView
        val recordButton = findViewById<View>(R.id.record_button) as RecordButton

        //IMPORTANT
        recordButton.setRecordView(recordView)
        //ListenForRecord must be false ,otherwise onClick will not be called
        recordButton.setOnRecordClickListener {
            Toast.makeText(this, "RECORD BUTTON CLICKED", Toast.LENGTH_SHORT).show()
            Log.d("RecordButton", "RECORD BUTTON CLICKED")
        }

        recordView.setOnBasketAnimationEndListener {
            Log.d(
                "RecordView",
                "Basket Animation Finished"
            )
        }

        //change slide To Cancel Text Color
        recordView.setSlideToCancelTextColor(Color.parseColor("#1A7AF6"));
        //change slide To Cancel Arrow Color
        recordView.setSlideToCancelArrowColor(Color.parseColor("#1A7AF6"));
        //change Counter Time (Chronometer) color
        recordView.setCounterTimeColor(Color.parseColor("#1A7AF6"));
        recordView.setOnRecordListener(object : OnRecordListener {
            override fun onStart() {
                //Start Recording..
                imageView_send.visibility = View.GONE
                imageView19.visibility = View.GONE
                imageView16.visibility = View.GONE
                fab_send_image.visibility = View.GONE
                if (CheckPermissions()) {

//                    mRecorder = MediaRecorder()
//                    mRecorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
//                    mRecorder!!.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
//                    mRecorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
//                    mRecorder!!.setOutputFile(mFileName)
                    try {
                        myAudioRecorder!!.prepare()
                    } catch (e: IOException) {
                        Log.e("check", e.toString())
                    }
                    myAudioRecorder!!.start()
                } else {
                    RequestPermissions()
                }
                Log.d("RecordView", "onStart")
            }

            override fun onCancel() {
                //On Swipe To Cancel
                imageView_send.visibility = View.VISIBLE
                imageView19.visibility = View.VISIBLE
                imageView16.visibility = View.VISIBLE
                fab_send_image.visibility = View.VISIBLE
                editText_message.visibility = View.VISIBLE

                Log.d("RecordView", "onCancel")

            }

            override fun onFinish(recordTime: Long) {
                //Stop Recording..
                imageView_send.visibility = View.VISIBLE
                imageView19.visibility = View.VISIBLE
                imageView16.visibility = View.VISIBLE
                fab_send_image.visibility = View.VISIBLE
                editText_message.visibility = View.VISIBLE
                myAudioRecorder!!.stop()
                myAudioRecorder!!.release()
                myAudioRecorder = null
                upload()
//                val time = getHumanTimeText(recordTime)
                Log.d("RecordView", recordTime.toString())

            }

            override fun onLessThanSecond() {
                imageView_send.visibility = View.VISIBLE
                imageView19.visibility = View.VISIBLE
                imageView16.visibility = View.VISIBLE
                fab_send_image.visibility = View.VISIBLE
                editText_message.visibility = View.VISIBLE
                //When the record time is less than One Second
                Log.d("RecordView", "onLessThanSecond")
            }
        })


//        AudioUtil.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)


        editText_message.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {}

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {
                if (s.toString().trim().isEmpty()) {
                    recordButton.visibility = View.VISIBLE
                    imageView16.visibility = View.VISIBLE
                    fab_send_image.visibility = View.VISIBLE
                    imageView_send.visibility = View.VISIBLE
                    imageView21.visibility = View.INVISIBLE
                } else {
                    recordButton.visibility = View.GONE
                    imageView16.visibility = View.GONE
                    fab_send_image.visibility = View.GONE
                    imageView_send.visibility = View.GONE
                    imageView21.visibility = View.VISIBLE
                }
            }
        })
//        KeyboardVisibilityEvent.setEventListener(
//            this,
//            object : KeyboardVisibilityEventListener {
//                override fun onVisibilityChanged(isOpen: Boolean) {
//                    toast(isOpen.toString())
//                    // write your code
//                }
//            })
    }

    private fun upload() {
        uris = Uri.fromFile(File(mFileName))
//        progress = ProgressDialog(this)
//        progress!!.setMessage("Processing..")
//        progress!!.setCancelable(false)
//        progress!!.show()
        val mReference = mStorage.child(uris.lastPathSegment!!)
        try {
            mReference.putFile(uris)
                .addOnSuccessListener { taskSnapshot: UploadTask.TaskSnapshot? ->
                    val result = taskSnapshot!!.metadata!!.reference!!.downloadUrl
                    result.addOnSuccessListener {
                        //                    progress!!.dismiss()
                        val s = it.toString()
                        Log.e("ADffsd", s.toString())
                        post(s)

//                    postJob(dis, loca, qus1, qus2, type, credits, creditPoint, uri, namess, s)
                    }
                }
        } catch (e: Exception) {
//            progress!!.dismiss()
        }

    }

    fun post(s: String) {
        val timestamp = Calendar.getInstance().timeInMillis
        val messageToSend =
            AudioMessage(
                s, message.size, Calendar.getInstance().time
                , timestamp,
                sessionMaintainence!!.firstName!!,
                sessionMaintainence!!.fullname!!, "Guest", currentChannelId
            )
//        insert(messageToSend)
        FirestoreUtil.sendMessage(messageToSend, currentChannelId)
    }

    @SuppressLint("MissingSuperCall")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RC_SELECT_IMAGE && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null
        ) {
            val selectedImagePath = data.data

            val imagePath = FirestoreUtil.getRealPathFromURI(selectedImagePath!!, this)

            val selectedImageBmp =
                MediaStore.Images.Media.getBitmap(contentResolver, selectedImagePath)

            val outputStream = ByteArrayOutputStream()

            selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 50, outputStream)
            val selectedImageBytes = outputStream.toByteArray()

            val timestamp = Calendar.getInstance().timeInMillis

            StorageUtil.SaveImage(selectedImageBmp, timestamp, "jpg")

            val messageToSend =
                ImageMessage(
                    selectedImagePath.toString(),
                    selectedImagePath.toString(),
                    "",
                    message.size,
                    Calendar.getInstance().time,
                    timestamp,
                    "fcffbc55e",
                    "08d3820b2",
                    "Guest",
                    currentChannelId
                )
            message.add(
                ImageMessageItem(
                    messageToSend,
                    this,
                    selectedImageBytes,
                    currentChannelId,
                    selectedImageBmp,
                    message.size,
                    timestamp
                )
            )
            updateRecyclerView1(message)

            insert(messageToSend, this@ChatPageActivity, false)

//            FirestoreUtil.sendMessage(messageToSend, currentChannelId)

        }
        if (requestCode == RC_SELECT_VIDEO && resultCode == Activity.RESULT_OK &&
            data != null && data.data != null
        ) {
            urisdd = data.data!!

            var mReference = mStorage.child(urisdd.lastPathSegment!!)
            val timestamp = Calendar.getInstance().timeInMillis

            StorageUtil.copyFile(this@ChatPageActivity, data.data!!, timestamp)
            postVideo(timestamp, urisdd.lastPathSegment!!, mReference, urisdd)
        }
        try {
            if (resultCode == Activity.RESULT_OK) {
                //set image captured to image view
                val selectedImagePath = data?.data
                val selectedImageBmp = MediaStore.Images.Media.getBitmap(contentResolver, image_uri)

                val outputStream = ByteArrayOutputStream()

                selectedImageBmp.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
                val selectedImageBytes = outputStream.toByteArray()
                val timestamp = Calendar.getInstance().timeInMillis
                StorageUtil.SaveImage(selectedImageBmp, timestamp, "jpg")
                val messageToSend =
                    ImageMessage(
                        selectedImagePath.toString(),
                        selectedImagePath.toString(),
                        "",
                        message.size,
                        Calendar.getInstance().time,
                        timestamp,
                        "fcffbc55e",
                        "08d3820b2",
                        "Guest",
                        currentChannelId
                    )
                message.add(
                    ImageMessageItem(
                        messageToSend,
                        this,
                        selectedImageBytes,
                        currentChannelId,
                        selectedImageBmp,
                        message.size,
                        timestamp
                    )
                )
                updateRecyclerView1(message)
                insert(messageToSend, this@ChatPageActivity,false)
            }
        } catch (e: Exception) {
        }
    }

    private fun postVideo(timestamp: Long, s: String, mReference: StorageReference, urisdd: Uri) {
        val messageToSend =
            VideoMessage(
                s, message.size, Calendar.getInstance().time, timestamp,
                "fcffbc55e",
                "08d3820b2", "Guest", currentChannelId
            )
        message.add(
            VideoMessageItem(
                messageToSend,
                this,
                mReference,
                urisdd,
                currentChannelId,
                timestamp
            )
        )
        updateRecyclerView1(message)
        insert(messageToSend, this@ChatPageActivity, false)
//        FirestoreUtil.sendMessage(messageToSend, currentChannelId)
    }

    @SuppressLint("StaticFieldLeak")
    private fun loadUser(channelId: String) {

        object : AsyncTask<Void?, Void?, Void?>() {
            override fun doInBackground(vararg p0: Void?): Void? {
                messageList = FirestoreUtil.getMessage("fcffbc55e", "08d3820b2")

                if (messageList.size > 0) {
                    messageList.forEach {
                        if (it.type == MessageType.TEXT) {
                            items.add(
                                TextMessageItem(
                                    FirestoreUtil.convertMessageUserToTextMessage(
                                        it
                                    ), true, this@ChatPageActivity
                                )
                            )
                        } else if (it.type == MessageType.IMAGE) {
                            items.add(
                                ImageMessageItem(
                                    FirestoreUtil.convertMessageUserToImageMessage(it),
                                    this@ChatPageActivity,
                                    null,
                                    null,
                                    null,
                                    0,
                                    null
                                )
                            )
                        } else if (it.type == MessageType.AUDIO) {
//                            items.add(
//                                AudioMessageItem(
//                                FirestoreUtil.convertMessageUserToAudioMessage(it),
//                                this@ChatPageActivity,
//                                null,
//                                null,
//                                null,
//                                0,
//                                null
//                            )
//                            )
                        } else {
                            items.add(
                                VideoMessageItem(
                                    FirestoreUtil.convertMessageUserToVideoMessage(it),
                                    this@ChatPageActivity,
                                    null,
                                    null,
                                    null,
                                    null
                                )
                            )
                        }
                    }
                    runOnUiThread(object : Runnable {
                        override fun run() {
                            updateRecyclerView(items)
                        }
                    })

                    FirestoreUtil.addChatMessagesListener(this@ChatPageActivity,
                        channelId,
                        this@ChatPageActivity,
                        this@ChatPageActivity::updateRecyclerView)
                } else {
                    messagesListenerRegistration =
                        FirestoreUtil.addChatMessagesListener(this@ChatPageActivity,
                            channelId,
                            this@ChatPageActivity,
                            this@ChatPageActivity::updateRecyclerView
                        )

                }
                return null
            }
        }.execute()

    }

    private fun updateRecyclerView(messages: MutableList<Item>) {
        recycler_view_messages.setHasFixedSize(true)
        fun init() {
            recycler_view_messages.apply {
                layoutManager = LinearLayoutManager(this@ChatPageActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(messages)
                    this.add(this@ChatPageActivity.messagesSection)
                }
            }
            message = messages
            shouldInitRecyclerView = false
        }
        message = messages
        fun updateItems() = messagesSection.update(messages)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

        recycler_view_messages.scrollToPosition(recycler_view_messages.adapter!!.itemCount - 1)
    }

    private fun updateRecyclerView1(messages: MutableList<Item>) {
        recycler_view_messages.setHasFixedSize(true)
        fun init() {
            recycler_view_messages.apply {
                layoutManager = LinearLayoutManager(this@ChatPageActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(messages)
                    this.add(this@ChatPageActivity.messagesSection)
                }
            }
            message = messages
            shouldInitRecyclerView = false
        }

        fun updateItems() = messagesSection.update(messages)

        updateItems()

        try {
            recycler_view_messages.scrollToPosition(recycler_view_messages.adapter!!.itemCount - 1)
        }catch (ex: NullPointerException){
            init()
            recycler_view_messages.scrollToPosition(recycler_view_messages.adapter!!.itemCount - 1)
        }

    }

    private fun updateMessage(messages: MutableList<Item>) {

        fun updateItems() = messagesSection.update(messages)

        updateItems()

        recycler_view_messages.scrollToPosition(recycler_view_messages.adapter!!.itemCount - 1)
    }

    private fun openCamera() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        image_uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        //camera intent
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri)
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //called when user presses ALLOW or DENY from Permission Request Popup
        when (requestCode) {
            REQUEST_AUDIO_PERMISSION_CODE -> if (grantResults.isNotEmpty()) {
                val permissionToRecord = grantResults[0] == PackageManager.PERMISSION_GRANTED
                val permissionToStore = grantResults[1] == PackageManager.PERMISSION_GRANTED
                if (permissionToRecord && permissionToStore) {
                } else {
                    Toast.makeText(applicationContext, "Permission Denied", Toast.LENGTH_LONG)
                        .show()
                }
            }
            PERMISSION_CODE -> {

                if (grantResults.size > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup was granted
                    openCamera()
                } else {
                    //permission from popup was denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    companion object {
        private const val REQUEST_RECORD_AUDIO = 0
        private const val LOG_TAG = "AudioRecording"
        private var mFileName: String? = null
        const val REQUEST_AUDIO_PERMISSION_CODE = 1
    }


    fun CheckPermissions(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val result1 =
            ContextCompat.checkSelfPermission(applicationContext, Manifest.permission.RECORD_AUDIO)
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    fun ChecksPermissions(): Boolean {
        val result = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val result1 = ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        return result == PackageManager.PERMISSION_GRANTED && result1 == PackageManager.PERMISSION_GRANTED
    }

    private fun RequestPermissions() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), REQUEST_AUDIO_PERMISSION_CODE
        )
    }

    private fun RequestPermissionss() {
        ActivityCompat.requestPermissions(
            this, arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION
            ), REQUEST_AUDIO_PERMISSION_CODE
        )
    }

    override fun chatMessage(messageItem: TextMessage) {
        items.add(TextMessageItem(messageItem, true,this@ChatPageActivity))
        runOnUiThread { updateRecyclerView1(items) }
    }

    override fun chatMessage(messageItem: ImageMessage) {
        items.add(ImageMessageItem(messageItem, this@ChatPageActivity, null,
            null,
            null,
            0,
            null))
        runOnUiThread { updateRecyclerView1(items) }
    }

    override fun chatMessage(messageItem: VideoMessage) {
        items.add(VideoMessageItem(messageItem, this@ChatPageActivity,null,
            null,
            null,
            null))
        runOnUiThread { updateRecyclerView1(items) }
    }

    override fun chatMessage(messageItem: AudioMessage) {
        items.add(AudioMessageItem(messageItem, this@ChatPageActivity))
        runOnUiThread { updateRecyclerView1(items) }
    }
}
