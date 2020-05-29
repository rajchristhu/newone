package com.focuzar.holoassist.util

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.app.adprogressbarlib.AdCircleProgress

import com.focuzar.holoassist.utilities.SessionMaintainence
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import java.io.File
import java.io.File.separator
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.util.*


object StorageUtil {
    private val storageInstance: FirebaseStorage by lazy { FirebaseStorage.getInstance() }
    var sessionMaintainence = SessionMaintainence.instance

    private val currentUserRef: StorageReference
        get() = storageInstance.reference
            .child(
                sessionMaintainence!!.firstName
                    ?: throw NullPointerException("UID is null.")
            )

    fun uploadProfilePhoto(
        imageBytes: ByteArray,
        onSuccess: (imagePath: String) -> Unit
    ) {
        val ref = currentUserRef.child("profilePictures/${UUID.nameUUIDFromBytes(imageBytes)}")
        ref.putBytes(imageBytes)
            .addOnSuccessListener {
                onSuccess(ref.path)
            }
    }

    fun uploadMessageImage(
        imageBytes: ByteArray, progress: AdCircleProgress?,
        onSuccess: (imagePath: String) -> Unit
    ) {
        val ref = currentUserRef.child("messages/${UUID.nameUUIDFromBytes(imageBytes)}")
        ref.putBytes(imageBytes)
            .addOnSuccessListener {
                if (progress != null) {
                    progress.setAdProgress(100)
                    progress.visibility = View.GONE
                }
                onSuccess(ref.path)
            }.addOnProgressListener { task ->
                //calculating progress percentage
                val progressValue: Double = (100.0 * task.bytesTransferred) / task.totalByteCount;
                progress?.setAdProgress(progressValue.toInt())
            };
    }

    fun pathToReference(path: String) = storageInstance.getReference(path)

    fun SaveImage(finalBitmap: Bitmap, timestamp: Long, extension: String) {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/holoAsisist/images/sent")
        if (!myDir.exists()) {
            myDir.mkdirs()
        }
        val fname = "Image-$timestamp.$extension"
        val file = File(myDir, fname)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun copyFile(context: Context, fileUri: Uri, timestamp: Long) {
        var inputStream: InputStream? = null
        var outputStream: OutputStream? = null

        try {
            val content: ContentResolver = context.getContentResolver()
            inputStream = content.openInputStream(fileUri)
            val root = Environment.getExternalStorageDirectory().toString()+ "/holoAsisist/images/sent"

            // create a directory
            val myDir = File(root)
            if (!myDir.exists()) {
                myDir.mkdirs()
            }
            val fname = "Image-$timestamp.mp4"
            val file = File(myDir, fname)
//            val saveDirectory =
//                File(Environment.getExternalStorageDirectory() + separator.toString() + "directory_name" + separator)
//            // create direcotory if it doesn't exists
//            saveDirectory.mkdirs()
            outputStream =
                FileOutputStream(file) // filename.png, .mp3, .mp4 ...
            if (outputStream != null) {
                Log.e("", "Output Stream Opened successfully")
            }
            val buffer = ByteArray(1000)
            var bytesRead = 0
            while (inputStream!!.read(buffer, 0, buffer.size).also { bytesRead = it } >= 0) {
                outputStream?.write(buffer, 0, buffer.size)
            }
        } catch (e: java.lang.Exception) {
            Log.e("TAG", "Exception occurred " + e.message)
        } finally {
        }
    }

    fun getImageFromPath(timestamp: Long, folder: String): Bitmap? {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/holoAsisist/images/$folder/Image-$timestamp.jpg")
        val bitmap = BitmapFactory.decodeFile(myDir.path)
        return bitmap
    }

    fun downloadFile(
        url: String,
        timestamp: Long,
        progress: AdCircleProgress,
        imageView: ImageView,
        extension: String
    ) {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/holoAsisist/images/received")
        if (!myDir.exists()) {
            myDir.mkdirs()
        }
        val fname = "Image-$timestamp.$extension"
        val file = File(myDir, fname)
        val httpsReference = Firebase.storage.getReferenceFromUrl(pathToReference(url).toString())
//        val localFile = File.createTempFile("Image1-$timestamp", ".jpg")
        httpsReference.getFile(file).addOnSuccessListener {
            if (progress != null) {
                progress.setAdProgress(100)
                progress.visibility = View.GONE
                imageView.setImageBitmap(getImageFromPath(timestamp, "received"))
            }
        }.addOnFailureListener {

        }.addOnProgressListener {
            //calculating progress percentage
            val progressValue: Double = (100.0 * it.bytesTransferred) / it.totalByteCount;
            progress?.setAdProgress(progressValue.toInt())
        }
    }


    fun downloadVideoFile(context: Context,
        url: String,
        timestamp: Long,
        progress: AdCircleProgress,
        imageView: ImageView,
        extension: String
    ) {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/holoAsisist/images/received")
        if (!myDir.exists()) {
            myDir.mkdirs()
        }
        val fname = "Image-$timestamp.$extension"
        val file = File(myDir, fname)


        val httpsReference = Firebase.storage.getReferenceFromUrl(url)
//        val localFile = File.createTempFile("Image1-$timestamp", ".jpg")
        httpsReference.getFile(file).addOnSuccessListener {
            if (progress != null) {
                progress.setAdProgress(100)
                progress.visibility = View.GONE

//                GlideApp.with(context)
//                    .load(file.absoluteFile)
//                    .placeholder(R.drawable.ic_image_black_24dp)
//                    .into(imageView)
//                imageView.setImageBitmap(getImageFromPath(timestamp, "received"))
            }
        }.addOnFailureListener {

        }.addOnProgressListener {
            //calculating progress percentage
            val progressValue: Double = (100.0 * it.bytesTransferred) / it.totalByteCount;
            progress?.setAdProgress(progressValue.toInt())
        }
    }


    fun isFileExist(timestamp: Long, folder: String, extension: String): Boolean {
        val root = Environment.getExternalStorageDirectory().toString()
        val myDir = File("$root/holoAsisist/images/$folder")
        if (!myDir.exists()) {
            myDir.mkdirs()
        }
        val fname = "Image-$timestamp.$extension"
        val file = File(myDir, fname)
        return file.exists()
    }
}