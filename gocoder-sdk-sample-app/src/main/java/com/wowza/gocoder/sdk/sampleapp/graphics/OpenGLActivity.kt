/**
 * This is sample code provided by Wowza Media Systems, LLC.  All sample code is intended to be a reference for the
 * purpose of educating developers, and is not intended to be used in any production environment.
 *
 *
 * IN NO EVENT SHALL WOWZA MEDIA SYSTEMS, LLC BE LIABLE TO YOU OR ANY PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL,
 * OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF WOWZA MEDIA SYSTEMS, LLC HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * WOWZA MEDIA SYSTEMS, LLC SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. ALL CODE PROVIDED HEREUNDER IS PROVIDED "AS IS".
 * WOWZA MEDIA SYSTEMS, LLC HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *
 * © 2015 – 2019 Wowza Media Systems, LLC. All rights reserved.
 *
 *
 * This is sample code provided by Wowza Media Systems, LLC.  All sample code is intended to be a reference for the
 * purpose of educating developers, and is not intended to be used in any production environment.
 *
 *
 * IN NO EVENT SHALL WOWZA MEDIA SYSTEMS, LLC BE LIABLE TO YOU OR ANY PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL,
 * OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF WOWZA MEDIA SYSTEMS, LLC HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * WOWZA MEDIA SYSTEMS, LLC SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. ALL CODE PROVIDED HEREUNDER IS PROVIDED "AS IS".
 * WOWZA MEDIA SYSTEMS, LLC HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *
 * © 2015 – 2019 Wowza Media Systems, LLC. All rights reserved.
 *
 *
 * This is sample code provided by Wowza Media Systems, LLC.  All sample code is intended to be a reference for the
 * purpose of educating developers, and is not intended to be used in any production environment.
 *
 *
 * IN NO EVENT SHALL WOWZA MEDIA SYSTEMS, LLC BE LIABLE TO YOU OR ANY PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL,
 * OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF WOWZA MEDIA SYSTEMS, LLC HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *
 * WOWZA MEDIA SYSTEMS, LLC SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. ALL CODE PROVIDED HEREUNDER IS PROVIDED "AS IS".
 * WOWZA MEDIA SYSTEMS, LLC HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 *
 * © 2015 – 2019 Wowza Media Systems, LLC. All rights reserved.
 */


/**
 * This is sample code provided by Wowza Media Systems, LLC.  All sample code is intended to be a reference for the
 * purpose of educating developers, and is not intended to be used in any production environment.
 *
 * IN NO EVENT SHALL WOWZA MEDIA SYSTEMS, LLC BE LIABLE TO YOU OR ANY PARTY FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL,
 * OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS, ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION,
 * EVEN IF WOWZA MEDIA SYSTEMS, LLC HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * WOWZA MEDIA SYSTEMS, LLC SPECIFICALLY DISCLAIMS ANY WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. ALL CODE PROVIDED HEREUNDER IS PROVIDED "AS IS".
 * WOWZA MEDIA SYSTEMS, LLC HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
 *
 * © 2015 – 2019 Wowza Media Systems, LLC. All rights reserved.
 */

package com.wowza.gocoder.sdk.sampleapp.graphics

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.EGL14
import android.opengl.GLES20
import android.opengl.GLSurfaceView
import android.os.*
import android.preference.PreferenceManager
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.ar.core.*
import com.google.ar.core.exceptions.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.storage.FirebaseStorage
import com.wowza.gocoder.sdk.api.android.opengl.WOWZGLES
import com.wowza.gocoder.sdk.api.broadcast.WOWZGLBroadcaster
import com.wowza.gocoder.sdk.api.devices.WOWZAudioDevice
import com.wowza.gocoder.sdk.api.devices.WOWZCameraView
import com.wowza.gocoder.sdk.api.geometry.WOWZSize
import com.wowza.gocoder.sdk.api.logging.WOWZLog
import com.wowza.gocoder.sdk.api.render.WOWZRenderAPI
import com.wowza.gocoder.sdk.sampleapp.GoCoderSDKActivityBase
import com.wowza.gocoder.sdk.sampleapp.R
import com.wowza.gocoder.sdk.sampleapp.common.helpers.*
import com.wowza.gocoder.sdk.sampleapp.common.rendering.BackgroundRenderer
import com.wowza.gocoder.sdk.sampleapp.common.rendering.ObjectRenderer
import com.wowza.gocoder.sdk.sampleapp.common.rendering.PlaneRenderer
import com.wowza.gocoder.sdk.sampleapp.common.rendering.PointCloudRenderer
import com.wowza.gocoder.sdk.sampleapp.config.GoCoderSDKPrefs
import com.wowza.gocoder.sdk.sampleapp.graphics.models.ImageModel
import com.wowza.gocoder.sdk.sampleapp.ui.MultiStateButton
import com.wowza.gocoder.sdk.sampleapp.ui.StatusView
import com.wowza.gocoder.sdk.support.status.WOWZStatus
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.IntBuffer
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class OpenGLActivity : GoCoderSDKActivityBase(), GLSurfaceView.Renderer, WOWZRenderAPI.VideoFrameRenderer {
    private var mFirstFrameTimestampNs = -1L
    private var viewHeight = 0
    private var rectRenderer: RectanglePolygonRenderer? = null
    private var viewWidth = 0
    var streaming = false
    var imageList = mutableListOf<ImageModel>()
    var imageLists = mutableListOf<ImageModel>()
    var image: ImageModel? = null
//    private var firestoreListener: ListenerRegistration? = null

    protected var mWZAudioDevice: WOWZAudioDevice? = null
    private val mGrabFrame = AtomicBoolean(false)
    private val mSavingFrame = AtomicBoolean(false)
    private var mWZCameraView: WOWZCameraView? = null
    val outputJpegFile: File?
        get() {
            val mediaStorageDir = File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "GoCoderSDK Screenshots")
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    return null
                }
            }
            @SuppressLint("SimpleDateFormat")
            val timeStamp = SimpleDateFormat("yyyy-MM-dd-HHmmss").format(Date())

            return File(mediaStorageDir.path + File.separator + timeStamp + ".jpg")
        }
    // Rendering. The Renderers are created here, and initialized when the GL surface is created.

    var handler: Handler? = null
    var firestoreDB: FirebaseFirestore? = null
    var myBitmap: Bitmap? = null
    var mWidth: Int? = null;
    var mHeight: Int? = null;
    var capturePicture = false;
    internal lateinit var frame1: Frame
    internal lateinit var camera1: Camera
    internal lateinit var cp: Collection<Plane>
    var screenshot = false
    private var installRequested: Boolean = false
    private var mDevicesInitialized = false

    private var session: Session? = null
    private var displayRotationHelper: DisplayRotationHelper? = null
    private val trackingStateHelper = TrackingStateHelper(this)
    private var tapHelper: TapHelper? = null
    private val mRenderingLock = Any()
    private var mRendering = false
    private val backgroundRenderer = BackgroundRenderer()
    private val virtualObject = ObjectRenderer()
    private val virtualObjectShadow = ObjectRenderer()
    private val planeRenderer = PlaneRenderer()
    private val pointCloudRenderer = PointCloudRenderer()
    var currentDate = ""
    // Temporary matrix allocated here to reduce number of allocations for each frame.
    private val anchorMatrix = FloatArray(16)
    var glBroadcaster: WOWZGLBroadcaster? = null
        private set

    var surfaceSize: WOWZSize? = null
        private set

    private val isStreaming: Boolean
        get() = glBroadcaster != null && !glBroadcaster!!.broadcasterStatus.isIdle

    private val anchors = ArrayList<ColoredAnchor>()
    protected var mBtnBroadcast: MultiStateButton? = null
    protected var mBtnSettings: MultiStateButton? = null
    protected var mStatusView: StatusView? = null

    private var surfaceView: GLSurfaceView? = null
    private val mOpenGLRenderer: OpenGLRenderer? = null

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        FullScreenHelper.setFullScreenOnWindowFocusChanged(this, hasFocus)
    }

    override fun onSurfaceCreated(gl10: GL10, eglConfig: EGLConfig) {
        GLES20.glClearColor(0.1f, 0.1f, 0.1f, 1.0f)

        // Prepare the rendering objects. This involves reading shaders, so may throw an IOException.
        try {
            // Create the texture and pass it to ARCore session to be filled during update().
            backgroundRenderer.createOnGlThread(/*context=*/this)
            planeRenderer.createOnGlThread(/*context=*/this, "models/trigrid.png")
            pointCloudRenderer.createOnGlThread(/*context=*/this)
            rectRenderer = RectanglePolygonRenderer()

            virtualObject.createOnGlThread(/*context=*/this, "models/andy.obj", "models/andy.png")
            virtualObject.setMaterialProperties(0.0f, 2.0f, 0.5f, 6.0f)

            virtualObjectShadow.createOnGlThread(
                    /*context=*/ this, "models/andy_shadow.obj", "models/andy_shadow.png")
            virtualObjectShadow.setBlendMode(ObjectRenderer.BlendMode.Shadow)
            virtualObjectShadow.setMaterialProperties(1.0f, 0.0f, 0.0f, 1.0f)

        } catch (e: IOException) {
            Log.e(TAG, "Failed to read an asset file", e)
        }

    }

    fun onStreamStart() {
        if (glBroadcaster == null || isStreaming) return
        // Initialize the timestamp tracker
        mFirstFrameTimestampNs = -1L
    }


    private fun encodeFrame() {
        if (glBroadcaster != null && glBroadcaster!!.broadcasterStatus.isRunning) {
            val currentTimeNs = System.nanoTime()
            if (mFirstFrameTimestampNs == -1L)
                mFirstFrameTimestampNs = currentTimeNs
            val frameTimestampNs = currentTimeNs - mFirstFrameTimestampNs
            //            Log.d("checkas",frameTimestampNs);
            glBroadcaster!!.onFrameAvailable(frameTimestampNs)
        }
    }

    /**
     * Called to indicate streaming has stopped
     */
    fun onStreamEnd() {
        if (glBroadcaster == null) return

        glBroadcaster!!.videoSourceConfig.videoFrameSize = surfaceSize
    }

    /* WOWZRenderAPI.VideoFrameRenderer methods ----------------------------------------------------------------------------------------- */


    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {

        displayRotationHelper!!.onSurfaceChanged(width, height)
        // Set the OpenGL viewport to the same size as the surface.
        GLES20.glViewport(0, 0, width, height)
        viewWidth = width
        viewHeight = height
        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        val ratio = width.toFloat() / height
        val left = -ratio
        val right = ratio
        val bottom = -1.0f
        val top = 1.0f
        val near = 1.0f
        val far = 10.0f


        if (glBroadcaster == null) {
            glBroadcaster = WOWZGLBroadcaster(EGL14.eglGetCurrentContext())
            glBroadcaster!!.videoFrameRenderer = this

            // Initialize the surface in the broadcaster's video source config
            surfaceSize = WOWZSize(width, height)
            glBroadcaster!!.videoSourceConfig.videoFrameSize = surfaceSize
        } else {
            surfaceSize!!.set(width, height)
            if (!isStreaming)
            // don't change the frame size in the video source config while streaming
                glBroadcaster!!.videoSourceConfig.videoFrameSize = surfaceSize
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onDrawFrame(gl10: GL10) {
        // Clear screen to notify driver it should not load any pixels from previous frame.

        if (viewWidth == 0 || viewWidth == 0) {
            return
        }
        if (session == null) {
            return
        }
        displayRotationHelper!!.updateSessionIfNeeded(session)
        session!!.setCameraTextureName(backgroundRenderer.textureId)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        //        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        // Start by clearing the alpha channel of the color buffer to 1.0.
        GLES20.glClearColor(1f, 1f, 1f, 1f)
        //        GLES20.glColorMask(false, false, false, true);
        //        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //        GLES20.glColorMask(true, true, true, true);
        //
        //        // Disable depth write.
        //        GLES20.glDepthMask(false);
        //
        //        // Additive blending, masked by alpha channel, clearing alpha channel.
        //        GLES20.glEnable(GLES20.GL_BLEND);
        //        GLES20.glBlendFuncSeparate(
        //                GLES20.GL_DST_ALPHA, GLES20.GL_ONE, // RGB (src, dest)
        //                GLES20.GL_ZERO, GLES20.GL_ONE_MINUS_SRC_ALPHA); // ALPHA (src, dest)

        // Notify ARCore session that the view size changed so that the perspective matrix and
        // the video background can be properly adjusted.


        //renderFrame();


        try {
            //            gainRenderingLock();


            // Obtain the current frame from ARSession. When the configuration is set to
            // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
            // camera framerate.
            val frame = session!!.update()
            frame1 = frame
            val camera = frame.camera
            camera1 = camera


            // Handle one tap per frame.
            handleTap(frame, camera)

            // If frame is ready, render camera preview image to the GL surface.
            backgroundRenderer.draw(frame)

            // Keep the screen unlocked while tracking, but allow it to lock when tracking stops.
            trackingStateHelper.updateKeepScreenOnFlag(camera.trackingState)

            // If not tracking, don't draw 3D objects, show tracking failure reason instead.
            if (camera.trackingState == TrackingState.PAUSED) {
                //                Toast.makeText(this,TrackingStateHelper.getTrackingFailureReasonString(camera),Toast.LENGTH_SHORT).show();
                Log.e("value", TrackingStateHelper.getTrackingFailureReasonString(camera))
                Log.e("values", "come")

                return
            }

            // Get projection matrix.
            val projmtx = FloatArray(16)
            camera.getProjectionMatrix(projmtx, 0, 0.1f, 100.0f)

            // Get camera matrix and draw.
            val viewmtx = FloatArray(16)
            camera.getViewMatrix(viewmtx, 0)

            // Compute lighting from average intensity of the image.
            // The first three components are color scaling factors.
            // The last one is the average pixel intensity in gamma space.
            val colorCorrectionRgba = FloatArray(4)
            frame.lightEstimate.getColorCorrection(colorCorrectionRgba, 0)

            // Visualize tracked points.
            // Use try-with-resources to automatically release the point cloud.
            frame.acquirePointCloud().use { pointCloud ->
                pointCloudRenderer.update(pointCloud)
                pointCloudRenderer.draw(viewmtx, projmtx)
            }

            // No tracking error at this point. If we detected any plane, then hide the
            // message UI, otherwise show searchingPlane message.
            if (hasTrackingPlane()) {
                //                messageSnackbarHelper.hide(this);
            } else {
                //                Toast.makeText(this,SEARCHING_PLANE_MESSAGE,Toast.LENGTH_SHORT).show();
                Log.e("test", SEARCHING_PLANE_MESSAGE)
            }

            // Visualize planes.

            cp = session!!.getAllTrackables(Plane::class.java)
            planeRenderer.drawPlanes(cp, camera.displayOrientedPose, projmtx)
            //            gainRenderingLock();

            // Visualize anchors created by touch.
            val scaleFactor = 1.0f
            for (coloredAnchor in anchors) {
                if (coloredAnchor.anchor.trackingState != TrackingState.TRACKING) {
                    continue
                }
                // Get the current pose of an Anchor in world space. The Anchor pose is updated
                // during calls to session.update() as ARCore refines its estimate of the world.
                coloredAnchor.anchor.pose.toMatrix(anchorMatrix, 0)

                // Update and draw the model and its shadow.
                virtualObject.updateModelMatrix(anchorMatrix, scaleFactor)
                virtualObjectShadow.updateModelMatrix(anchorMatrix, scaleFactor)
                virtualObject.draw(viewmtx, projmtx, colorCorrectionRgba, coloredAnchor.color)
                virtualObjectShadow.draw(viewmtx, projmtx, colorCorrectionRgba, coloredAnchor.color)
            }
            for (i in 0 until anchors.size) {
                if (i == 3) {
                    var point0 = getPose(anchors[0])
                    val point1: Pose = getPose(anchors[3])
                    drawLine(point0, point1, viewmtx, projmtx)
                } else {
                    var point0 = getPose(anchors[i])
                    val point1: Pose = getPose(anchors[i + 1])
                    val s = (point0.xAxis + point1.xAxis)
                    Log.e("value check", s.toString())
                    drawLine(point0, point1, viewmtx, projmtx)
                }

                //                log("onDrawFrame()", "before drawObj()")
//                drawObj(point1, cube, viewmtx, projmtx, lightIntensity)
//                checkIfHit(cube, i)
//                log("onDrawFrame()", "before drawLine()")
//                point0 = point1
//                ss = point0
//                val distanceCm = (getDistance(point0, point1) * 1000).toInt() / 10.0f
//                total += distanceCm.toDouble()
//                sb.append(" + ").append(distanceCm)
            }


            //            long time = SystemClock.uptimeMillis() % 10000L;
            //            float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
            //            setCurrentAngle(angleInDegrees);
            // If streaming, render a 2nd pass to the video encoder
            //            releaseRenderingLock();
//            if (screenshot) {
//                val screenshotSize = width * height
//                var bb = ByteBuffer.allocateDirect(screenshotSize * 4)
//                bb.order(ByteOrder.nativeOrder())
//                gl.glReadPixels(0, 0, width, height, GL10.GL_RGBA, GL10.GL_UNSIGNED_BYTE, bb)
//                var pixelsBuffer = IntArray(screenshotSize)
//                bb.asIntBuffer().get(pixelsBuffer)
//                bb = null
//                val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
//                bitmap.setPixels(pixelsBuffer, screenshotSize - width, -width, 0, 0, width, height)
//                pixelsBuffer = null
//
//                val sBuffer = ShortArray(screenshotSize)
//                val sb = ShortBuffer.wrap(sBuffer)
//                bitmap.copyPixelsToBuffer(sb)
//
//                //Making created bitmap (from OpenGL points) compatible with Android bitmap
//                for (i in 0 until screenshotSize) {
//                    val v = sBuffer[i]
//                    sBuffer[i] = (v && 0x1f shl 11 or (v & 0x7e0) or (v & 0xf800 shr 11)).toShort()
//                }
//                sb.rewind()
//                bitmap.copyPixelsFromBuffer(sb)
//                lastScreenshot = bitmap
//
//                screenshot = false
//            }
        } catch (t: Throwable) {
            // Avoid crashing the application due to unhandled exceptions.
        }
        // set these flags so that this doesn't get called numerous times in parallel
        mSavingFrame.set(true)
        mGrabFrame.set(false)
        if (streaming) {
            streaming = false
            SavePicture()
        }
        if (isStreaming)
            encodeFrame()

    }

    private val mPoseTranslation = FloatArray(3)
    private val mPoseRotation = FloatArray(4)
    private fun getPose(anchor: ColoredAnchor): Pose {
        val pose = anchor.anchor.pose
        pose.getTranslation(mPoseTranslation, 0)
        pose.getRotationQuaternion(mPoseRotation, 0)
        return Pose(mPoseTranslation, mPoseRotation)
    }

    fun drawLine(pose0: Pose, pose1: Pose, viewmtx: FloatArray, projmtx: FloatArray) {
        val lineWidth = 0.002f
        val lineWidthH = lineWidth / viewHeight * viewWidth
        rectRenderer!!.setVerts(
                pose0.tx() - lineWidth, pose0.ty() + lineWidthH, pose0.tz() - lineWidth,
                pose0.tx() + lineWidth, pose0.ty() + lineWidthH, pose0.tz() + lineWidth,
                pose1.tx() + lineWidth, pose1.ty() + lineWidthH, pose1.tz() + lineWidth,
                pose1.tx() - lineWidth, pose1.ty() + lineWidthH, pose1.tz() - lineWidth,
                pose0.tx() - lineWidth, pose0.ty() - lineWidthH, pose0.tz() - lineWidth,
                pose0.tx() + lineWidth, pose0.ty() - lineWidthH, pose0.tz() + lineWidth,
                pose1.tx() + lineWidth, pose1.ty() - lineWidthH, pose1.tz() + lineWidth,
                pose1.tx() - lineWidth, pose1.ty() - lineWidthH, pose1.tz() - lineWidth
        )

        rectRenderer!!.draw(viewmtx, projmtx)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Throws(IOException::class)
    fun SavePicture() {
        val pixelData = IntArray(viewWidth * viewHeight)

        // Read the pixels from the current GL frame.
        val buf = IntBuffer.wrap(pixelData)
        buf.position(0)
        GLES20.glReadPixels(0, 0, viewWidth, viewHeight,
                GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buf)

        // Create a file in the Pictures/HelloAR album.
        val out = File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "GoCoderSDK Screenshots" +
                java.lang.Long.toHexString(System.currentTimeMillis()) + ".png")

        // Make sure the directory exists
        if (!out.getParentFile().exists()) {
            out.getParentFile().mkdirs()
        }

        // Convert the pixel data from RGBA to what Android wants, ARGB.
        val bitmapData = IntArray(pixelData.size)
        for (i in 0 until viewHeight) {
            for (j in 0 until viewWidth) {
                val p = pixelData[i * viewWidth + j]
                val b = p and 0x00ff0000 shr 16
                val r = p and 0x000000ff shl 16
                val ga = p and -0xff0100
                bitmapData[(viewHeight - i - 1) * viewWidth + j] = ga or r or b
            }
        }
        // Create a bitmap.
//        val bmp = Bitmap.createBitmap(bitmapData,
//                viewWidth, viewHeight, Bitmap.Config.HARDWARE)
//
//        // Write it to disk.
//        val fos = FileOutputStream(out)
//        bmp.compress(Bitmap.CompressFormat.PNG, 100, fos)
//        fos.flush()
//        fos.close()
//        uploadFile(bmp)
//        runOnUiThread(Runnable { showSnackbarMessage("Wrote " + out.getName(), false) })
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, results: IntArray) {
        if (!CameraPermissionHelper.hasCameraPermission(this)) {
            Toast.makeText(this, "Camera permission is needed to run this application", Toast.LENGTH_LONG)
                    .show()
            if (!CameraPermissionHelper.shouldShowRequestPermissionRationale(this)) {
                // Permission denied with checking "Do not ask again".
                CameraPermissionHelper.launchPermissionSettings(this)
            }
            finish()
        }
    }

    override fun isWZVideoFrameRendererActive(): Boolean {
        // should always be true for broadcasters
        return true
    }

    /**
     * Called at the beginning of a broadcast
     */
    override fun onWZVideoFrameRendererInit(eglEnv: WOWZGLES.EglEnv) {
        // Initialize the encoder thread's OpenGL ES context environment
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)

        //        // Set the background clear color to black.
        ////        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        //        // Start by clearing the alpha channel of the color buffer to 1.0.
        //        GLES20.glClearColor(1, 1, 1, 1);
        //        GLES20.glColorMask(false, false, false, true);
        //        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //        GLES20.glColorMask(true, true, true, true);
        //
        //        // Disable depth write.
        //        GLES20.glDepthMask(false);
        //
        //        // Additive blending, masked by alpha channel, clearing alpha channel.
        //        GLES20.glEnable(GLES20.GL_BLEND);
        //        GLES20.glBlendFuncSeparate(
        //                GLES20.GL_DST_ALPHA, GLES20.GL_ONE, // RGB (src, dest)
        //                GLES20.GL_ZERO, GLES20.GL_ONE_MINUS_SRC_ALPHA); // ALPHA (src, dest)
        // Use culling to remove back faces.
        //        GLES20.glEnable(GLES20.GL_CULL_FACE);
        //
        //        // Enable depth testing
        //        GLES20.glEnable(GLES20.GL_DEPTH_TEST);

        // Set the GL viewport to the surface size
        val surfaceSize = eglEnv.eglSurfaceSize
    }

    /**
     * The callback invoked in response to an call to [WOWZGLBroadcaster.onFrameAvailable] to render
     * the frame to be encoded
     * @param eglEnv The [WOWZGLES.EglEnv] instance describing the OpenGL ES environment used
     * to encode the video frames for streaming
     * @param frameSize The broadcast video frame size which will remain constant throughout a streaming session
     * @param frameRotation The video source's current orientation as set by a call to [WOWZGLBroadcaster.setFrameRotation]
     */
    override fun onWZVideoFrameRendererDraw(eglEnv: WOWZGLES.EglEnv, frameSize: WOWZSize, frameRotation: Int) {
        GLES20.glViewport(0, 0, frameSize.getWidth(), frameSize.getHeight())


        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        //        GLES20.glClearColor(0.0f, 0.0f, 1.0f, 1.0f);
        // Start by clearing the alpha channel of the color buffer to 1.0.
        GLES20.glClearColor(1f, 1f, 1f, 1f)

        //
        //        // Start by clearing the alpha channel of the color buffer to 1.0.
        //        GLES20.glClearColor(1, 1, 1, 1);
        //        GLES20.glColorMask(false, false, false, true);
        //        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        //        GLES20.glColorMask(true, true, true, true);
        //
        //        // Disable depth write.
        //        GLES20.glDepthMask(false);
        //
        //        // Additive blending, masked by alpha channel, clearing alpha channel.
        //        GLES20.glEnable(GLES20.GL_BLEND);
        //        GLES20.glBlendFuncSeparate(
        //                GLES20.GL_DST_ALPHA, GLES20.GL_ONE, // RGB (src, dest)
        //                GLES20.GL_ZERO, GLES20.GL_ONE_MINUS_SRC_ALPHA); // ALPHA (src, dest)

        renderFrame()


    }

    /**
     * Called at the end of a broadcast
     */
    override fun onWZVideoFrameRendererRelease(eglEnv: WOWZGLES.EglEnv) {
        // nothing to do here
    }


    // Anchors created from taps used for object placing with a given color.
    private class ColoredAnchor(val anchor: Anchor, val color: FloatArray)

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_opengl)
        handler = Handler()
        firestoreDB = FirebaseFirestore.getInstance()

        // Initialize the UI controls
        mBtnBroadcast = findViewById<View>(R.id.ic_broadcast) as MultiStateButton
        mBtnSettings = findViewById<View>(R.id.ic_settings) as MultiStateButton
        var button = findViewById<View>(R.id.button) as Button
        mStatusView = findViewById<View>(R.id.statusView) as StatusView

        // Initialize the OpenGL ES surface view
        surfaceView = findViewById<View>(R.id.gl_surface_view) as GLSurfaceView
        displayRotationHelper = DisplayRotationHelper(/*context=*/this)

        // Set up tap listener.
        tapHelper = TapHelper(/*context=*/this)
        surfaceView!!.setOnTouchListener(tapHelper)

//        firestoreDB!!.collection("ImageSize").get()
//                .addOnSuccessListener { document ->
//                    for (doc in document) {
//                        val s = doc.data["imageHeight"]
//                        imageLists.add(ImageModel(s.toString(), s.toString()))
////                        doc.toObjects(ImageModel::class.java)
//                    }
////                    imageList = document.toObjects(ImageModel::class.java)
////                    imageLists.clear()
////                    for (i in imageList) {
////                        imageLists.add(i)
////                    }
////                    for (i in 0..imageList!!.size!!) {
//                    Log.e("uyty", imageList.size.toString())
//                    Log.e("uytuy", imageList.toString())
////                    }
//
//                }
//                .addOnFailureListener {
//
//                }

//
//        val query = firestoreDB!!.collection("Check")
//        firestoreListener = query.addSnapshotListener { snapshots, e ->
//            Log.e("ca", snapshots!!.documentChanges.toString())
//            for (doc in snapshots!!.documentChanges) {
//                Log.e("sadsd", doc.document.data.toString())
//            }
////             snapshots!!.documentChanges.
//        }
//        button.setOnClickListener {

//        }
        if (GoCoderSDKActivityBase.sGoCoderSDK != null) {
//            mWZCameraView = findViewById<View>(R.id.camera_preview) as WOWZCameraView

            // Create an register a video frame listener with WZCameraPreview
            val videoFrameListener = object : WOWZRenderAPI.VideoFrameListener {

                override// onWZVideoFrameListenerFrameAvailable() will only be called nce isWZVideoFrameListenerActive() returns true
                fun isWZVideoFrameListenerActive(): Boolean {
                    // Only indicate the frame listener once the screenshot button has been pressed
                    // and when we're not in the process of saving a previous screenshot
                    return mGrabFrame.get() && !mSavingFrame.get()
                }

                override fun onWZVideoFrameListenerInit(eglEnv: WOWZGLES.EglEnv) {
                    // nothing needed for this example
                }

                override// onWZVideoFrameListenerFrameAvailable() is called when isWZVideoFrameListenerActive() = true
                // and a new frame has been rendered on the camera preview display surface
                fun onWZVideoFrameListenerFrameAvailable(eglEnv: WOWZGLES.EglEnv, frameSize: WOWZSize, frameRotation: Int, timecodeNanos: Long) {

                }

                override fun onWZVideoFrameListenerRelease(eglEnv: WOWZGLES.EglEnv) {
                    // nothing needed for this example
                }
            }

            // register our newly created video frame listener wth the camera preview display view
//            mWZCameraView!!.registerFrameListener(videoFrameListener)

            if (mPermissionsGranted) {
                // start the camera preview display and enable the screenshot button when it is ready
//                mWZCameraView!!.startPreview(object : WOWZCameraView.PreviewStatusListener {
//                    override fun onWZCameraPreviewStarted(camera: WOWZCamera, frameSize: WOWZSize, frameRate: Int) {
////                        mBtnScreenshot!!.isEnabled = true
////                        mBtnScreenshot!!.isClickable = true
//                    }
//
//                    override fun onWZCameraPreviewStopped(cameraId: Int) {
////                        mBtnScreenshot!!.isEnabled = false
////                        mBtnScreenshot!!.isClickable = false
//                    }
//
//                    override fun onWZCameraPreviewError(camera: WOWZCamera, error: WOWZError) {
////                        mBtnScreenshot!!.isEnabled = false
////                        mBtnScreenshot!!.isClickable = false
//                        displayErrorDialog(error)
//                    }
//                })
            }
        }


        button.setOnClickListener {
            for (i in 0..3) {

            }
            var mdisp = getWindowManager().getDefaultDisplay();
            var mdispSize = android.graphics.Point()
            mdisp.getSize(mdispSize);
            var maxX = mdispSize.x;
            var maxY = mdispSize.y;
            var arrayList = arrayListOf<model>()
            arrayList.add(model(0F, 0F))
            arrayList.add(model(maxX.toFloat(), 0F))
            arrayList.add(model(maxX.toFloat(), maxY.toFloat()))
            arrayList.add(model(0F, maxY.toFloat()))
            for (i in arrayList) {
                handler!!.post {
                    try {
//                    Log.e("checks", message)
                        val downTime = SystemClock.uptimeMillis()
                        val eventTime = SystemClock.uptimeMillis() + 100
//                    val ss=s[0].removePrefix("Client : ")
                        val x = i.x
                        val y = i.y
                        val metaState = 0
                        val motionEvent = MotionEvent.obtain(
                                downTime,
                                eventTime,
                                MotionEvent.ACTION_DOWN,
                                x,
                                y,
                                metaState
                        )

// Dispatch touch event to view
                        surfaceView!!.dispatchTouchEvent(motionEvent)
                        val motionEvents = MotionEvent.obtain(
                                downTime,
                                eventTime,
                                MotionEvent.ACTION_UP,
                                x,
                                y,
                                metaState
                        )
                        surfaceView!!.dispatchTouchEvent(motionEvents)

                    } catch (e: Exception) {
                        Log.e("checkss", e.toString())

                    }
                }

            }
            onTakeScreenshost()
        }
        // Set up renderer.
        surfaceView!!.preserveEGLContextOnPause = true
        surfaceView!!.setEGLContextClientVersion(2)
        surfaceView!!.setEGLConfigChooser(8, 8, 8, 8, 16, 0) // Alpha used for plane blending.
        surfaceView!!.setRenderer(this)
        surfaceView!!.renderMode = GLSurfaceView.RENDERMODE_CONTINUOUSLY
        surfaceView!!.setWillNotDraw(false)

        installRequested = false
        //        surfaceView.setEGLContextClientVersion(2);
        //
        //        mOpenGLRenderer = new OpenGLRenderer();
        //        surfaceView.setRenderer(mOpenGLRenderer);
    }

    //    protected fun hasDevicePermissionToAccess(callback: CameraActivityBase.PermissionCallbackInterface) {
//        this.callbackFunction = callback
//        if (mWZBroadcast != null) {
//            var result = true
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                result = if (mRequiredPermissions.size > 0) WowzaGoCoder.hasPermissions(this, mRequiredPermissions) else true
//                if (!result && !hasRequestedPermissions) {
//                    ActivityCompat.requestPermissions(this, mRequiredPermissions, PERMISSIONS_REQUEST_CODE)
//                    hasRequestedPermissions = true
//                } else {
//                    this.callbackFunction.onPermissionResult(result)
//                }
//            } else {
//                this.callbackFunction.onPermissionResult(result)
//            }
//        }
//    }
    fun onTakeScreenshost() {
        streaming = true
        // Setting mGrabFrame to true will trigger the video frame listener to become active
        if (!mGrabFrame.get() && !mSavingFrame.get()) {
//        mBtnScreenshot!!.isEnabled = false
//        mBtnScreenshot!!.isClickable = false
            mGrabFrame.set(true)
        }
    }

    override fun onResume() {
        syncUIControlState()

        // The activity must call the GL surface view's onResume() on activity onResume().
        super.onResume()
        if (!mDevicesInitialized) {
            initGoCoderDevices()
        }
//        this.hasDevicePermissionToAccess(object : PermissionCallbackInterface {
//
//            override fun onPermissionResult(result: Boolean) {
//                if (!mDevicesInitialized || result) {
//                    initGoCoderDevices()
//                }
//            }
//        })

        if (session == null) {
            var exception: Exception? = null
            var message: String? = null
            try {
                when (ArCoreApk.getInstance().requestInstall(this, !installRequested)) {
                    ArCoreApk.InstallStatus.INSTALL_REQUESTED -> {
                        installRequested = true
                        return
                    }
                    ArCoreApk.InstallStatus.INSTALLED -> {
                    }
                }

                // ARCore requires camera permissions to operate. If we did not yet obtain runtime
                // permission on Android M and above, now is a good time to ask the user for it.
                if (!CameraPermissionHelper.hasCameraPermission(this)) {
                    CameraPermissionHelper.requestCameraPermission(this)
                    return
                }

                // Create the session.
                try {
                    session = Session(/* context= */this)
                } finally {

                }

            } catch (e: UnavailableArcoreNotInstalledException) {
                message = "Please install ARCore"
                exception = e
            } catch (e: UnavailableUserDeclinedInstallationException) {
                message = "Please install ARCore"
                exception = e
            } catch (e: UnavailableApkTooOldException) {
                message = "Please update ARCore"
                exception = e
            } catch (e: UnavailableSdkTooOldException) {
                message = "Please update this app"
                exception = e
            } catch (e: UnavailableDeviceNotCompatibleException) {
                message = "This device does not support AR"
                exception = e
            } catch (e: Exception) {
                message = "Failed to create AR session"
                exception = e
            }

            if (message != null) {
                //                messageSnackbarHelper.showError(this, message);
                Log.e(TAG, "Exception creating session", exception)
                return
            }
        }

        // Note that order matters - see the note in onPause(), the reverse applies here.
        try {
            session!!.resume()
        } catch (e: CameraNotAvailableException) {

            session = null
            return
        }

        surfaceView!!.onResume()
        displayRotationHelper!!.onResume()
    }

    override fun onPause() {
        // The activity must call the GL surface view's onPause() on activity onPause().
        super.onPause()
        //        surfaceView.onPause();
        if (session != null) {
            // Note that the order matters - GLSurfaceView is paused first so that it does not try
            // to query the session. If Session is paused before GLSurfaceView, GLSurfaceView may
            // still call session.update() and get a SessionPausedException.
            displayRotationHelper!!.onPause()
            surfaceView!!.onPause()
            session!!.pause()
        }
    }

    /**
     * Click handler for the broadcast button
     */
    fun onToggleBroadcast(v: View) {
        if (broadcast == null) return

        if (broadcast.status.isIdle) {
            if (!mWZBroadcastConfig.isVideoEnabled && !mWZBroadcastConfig.isAudioEnabled) run { Toast.makeText(this, "Unable to publish if both audio and video are disabled", Toast.LENGTH_LONG).show() }
            else {
                // Set the video stream broadcasting source to the WOWZGLBroadcaster created by the OpenGLRenderer
                mWZBroadcastConfig.videoBroadcaster = glBroadcaster

                // Get the current surface size to use as the video stream frame size
                val openGLSurfaceSize = surfaceSize
                mWZBroadcastConfig.videoFrameSize = openGLSurfaceSize
                mWZBroadcastConfig.videoSourceConfig.videoFrameSize = openGLSurfaceSize

                // Disable audio streaming
                mWZBroadcastConfig.isAudioEnabled = true

                val configError = startBroadcast()

                if (configError != null) {
                    if (mStatusView != null)
                        mStatusView!!.setErrorMessage(configError.errorDescription)
                }
            }
        } else {
            endBroadcast()
        }
    }

    /**
     * Click handler for the settings button
     */
    protected fun initGoCoderDevices() {
        if (GoCoderSDKActivityBase.sGoCoderSDK != null) {
            var videoIsInitialized = false
            var audioIsInitialized = false

            // Initialize the camera preview
//            if (this.hasDevicePermissionToAccess(Manifest.permission.CAMERA)) {
//                if (mWZCameraView != null) {
//                    val availableCameras = mWZCameraView.getCameras()
//                    // Ensure we can access to at least one camera
//                    if (availableCameras.size > 0) {
//                        // Set the video broadcaster in the broadcast config
//                        broadcastConfig.videoBroadcaster = mWZCameraView
//                        videoIsInitialized = true
//
//
//                        object : Thread() {
//                            override fun run() {
//                                WOWZLog.debug("*** getOriginalFrameSizes - Get original frame size : ")
//                                prefsFragment.setActiveCamera(if (mWZCameraView != null) mWZCameraView.getCamera() else null)
//                                fragmentManager.beginTransaction().replace(android.R.id.content, prefsFragment).hide(prefsFragment).commit()
//                            }
//                        }.start()
//
//                    } else {
//                        mStatusView.setErrorMessage("Could not detect or gain access to any cameras on this device")
//                        broadcastConfig.isVideoEnabled = false
//                    }
//                }
//            }

            if (this.hasDevicePermissionToAccess(Manifest.permission.RECORD_AUDIO)) {
                // Initialize the audio input device interface
                mWZAudioDevice = WOWZAudioDevice()

                // Set the audio broadcaster in the broadcast config
                broadcastConfig.audioBroadcaster = mWZAudioDevice
                audioIsInitialized = true
            }

            if (videoIsInitialized && audioIsInitialized)
                mDevicesInitialized = true
        }
    }

    fun onSettings(v: View) {
        // Display the prefs fragment
        val prefsFragment = GoCoderSDKPrefs.PrefsFragment()
        if (this.hasDevicePermissionToAccess(Manifest.permission.CAMERA) && this.hasDevicePermissionToAccess(Manifest.permission.RECORD_AUDIO)) {
            WOWZLog.debug("*** getOriginalFrameSizes Do we have permission = yes")
            fragmentManager.beginTransaction()
                    .replace(android.R.id.content, prefsFragment)
                    .addToBackStack(null)
                    .show(prefsFragment)
                    .commit()
        } else {
            Toast.makeText(this, "You must enable audio / video access to update the settings", Toast.LENGTH_LONG).show()
        }
//        prefsFragment.setActiveCamera(if (mWZCameraView != null) mWZCameraView.getCamera() else null)

        //        prefsFragment.setFixedVideoSource(true);
        //        prefsFragment.setShowAudioPrefs(false);
        //        prefsFragment.setActiveCamera(mWZCameraView != null ? mWZCameraView.getCamera() : null);


        /*
        // Display the prefs fragment

        WOWZLog.debug("*** getOriginalFrameSizes showSettings1");
        prefsFragment.setActiveCamera(mWZCameraView != null ? mWZCameraView.getCamera() : null);

        WOWZLog.debug("*** getOriginalFrameSizes showSettings2");
        getFragmentManager().addOnBackStackChangedListener(backStackListener);
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, prefsFragment)
                .addToBackStack(null)
                .show(prefsFragment)
                .commit();
        WOWZLog.debug("*** getOriginalFrameSizes showSettings3");*/
    }

    /**
     * WOWZStatusCallback interface methods
     */
    override fun onWZStatus(goCoderStatus: WOWZStatus) {
        Handler(Looper.getMainLooper()).post {
            if (goCoderStatus.isRunning) {  // The stream has started
                // Keep the screen on while we are broadcasting
                window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                // Since we have successfully opened up the server connection, store the connection info for auto complete
                GoCoderSDKPrefs.storeHostConfig(PreferenceManager.getDefaultSharedPreferences(this@OpenGLActivity), mWZBroadcastConfig)

                // Tell the renderer that streaming has started
                onStreamStart()

            } else if (goCoderStatus.isIdle) { // The stream has stopped
                // Clear the "keep screen on" flag
                window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)

                // Tell the renderer that streaming has stopped
                onStreamEnd()
            }

            if (mStatusView != null) mStatusView!!.setStatus(goCoderStatus)
            syncUIControlState()
        }
    }

    override fun onWZError(goCoderStatus: WOWZStatus) {
        Handler(Looper.getMainLooper()).post {
            if (mStatusView != null) mStatusView!!.setStatus(goCoderStatus)
            syncUIControlState()
        }
    }

    private fun uploadFile(bitmap: Bitmap) {
        val sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")
        currentDate = sdf.format(Date())
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child("scnImage")
        val mountainImagesRef = storageRef.child("images/" + currentDate + "scnImage" + ".jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos)
        val data = baos.toByteArray()
        val uploadTask = mountainImagesRef.putBytes(data)
        uploadTask.addOnFailureListener {

            // Handle unsuccessful uploads
        }.addOnSuccessListener { taskSnapshot ->
            val result = taskSnapshot.metadata!!.reference!!.downloadUrl
            result.addOnSuccessListener {
                val imageLink = it
                Log.e("image", imageLink.toString())
                val city = hashMapOf(
                        "imageUrl" to imageLink.toString()
                )
                firestoreDB!!.collection("Check").document("image")
                        .set(city)
                        .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successfully written!") }
                        .addOnFailureListener { e -> Log.w(TAG, "Error writing document", e) }
//                if (type == "quick") {
//                    checkCredits("user", SessionMaintainence.instance!!.Uid, "4", "quick", imageLink)
//                } else if (type == "post") {
//                    checkCredits("user", SessionMaintainence.instance!!.Uid, "2", "normal", imageLink)
//                }
            }
        }


    }


    protected fun syncUIControlState(): Boolean {
        val disableControls = broadcast == null || !(broadcast.status.isIdle || broadcast.status.isBroadcasting)
        val isStreaming = broadcast != null && broadcast.status.isBroadcasting

        if (disableControls) {
            if (mBtnBroadcast != null) mBtnBroadcast!!.isEnabled = false
            if (mBtnSettings != null) mBtnSettings!!.isEnabled = false
        } else {
            if (mBtnBroadcast != null) {
                mBtnBroadcast!!.setState(isStreaming)
                mBtnBroadcast!!.isEnabled = true
            }
            if (mBtnSettings != null)
                mBtnSettings!!.isEnabled = !isStreaming
        }

        return disableControls

    }

    // Handle only one tap per frame, as taps are usually low frequency compared to frame rate.
    private fun handleTap(frame: Frame, camera: Camera) {
        val tap = tapHelper!!.poll()

        if (tap != null && camera.trackingState == TrackingState.TRACKING) {
            val point: android.graphics.Point? = getScreenCenter()
//            val hits = frame.hitTest(point.x.toFloat(), point.y.toFloat())

            for (hit in frame.hitTest(point!!.x.toFloat(), point.y.toFloat())) {
                // Check if any plane was hit, and if it was hit inside the plane polygon
                hit.trackable.createAnchor(hit.hitPose)

                val trackable = hit.trackable
                // Creates an anchor if a plane or an oriented point was hit.
                if ((trackable is Plane
                                && trackable.isPoseInPolygon(hit.hitPose)
                                && PlaneRenderer.calculateDistanceToPlane(hit.hitPose, camera.pose) > 0) || trackable is Point && trackable.orientationMode == Point.OrientationMode.ESTIMATED_SURFACE_NORMAL) {
                    // Hits are sorted by depth. Consider only closest hit on a plane or oriented point.
                    // Cap the number of objects created. This avoids overloading both the
                    // rendering system and ARCore.
                    if (anchors.size >= 20) {
                        anchors[0].anchor.detach()
                        anchors.removeAt(0)
                    }

                    // Assign a color to the object for rendering based on the trackable type
                    // this anchor attached to. For AR_TRACKABLE_POINT, it's blue color, and
                    // for AR_TRACKABLE_PLANE, it's green color.
                    val objColor: FloatArray
                    objColor = if (trackable is Point) {
                        floatArrayOf(66.0f, 133.0f, 244.0f, 255.0f)
                    } else if (trackable is Plane) {
                        floatArrayOf(139.0f, 195.0f, 74.0f, 255.0f)
                    } else {
                        DEFAULT_COLOR
                    }

                    // Adding an Anchor tells ARCore that it should track this position in
                    // space. This anchor is created on the Plane to place the 3D model
                    // in the correct position relative both to the world and to the plane.
                    anchors.add(ColoredAnchor(hit.createAnchor(), objColor))
                    break
                }
            }
        }
//        else { handleMoveEvent(nowTouchingPointIndex) }
    }

    /** Checks if we detected at least one plane.  */
    private fun hasTrackingPlane(): Boolean {
        for (plane in session!!.getAllTrackables(Plane::class.java)) {
            if (plane.trackingState == TrackingState.TRACKING) {
                return true
            }
        }
        return false
    }
//    private fun handleMoveEvent(nowSelectedIndex: Int) {
//        try {
//            if (showingTapPointX.size < 1 || queuedScrollDx.size < 2) {
//                // no action, don't move
//                return
//            }
//            if (nowTouchingPointIndex == DEFAULT_VALUE) {
//                // no selected cube, don't move
//                return
//            }
//            if (nowSelectedIndex >= showingTapPointX.size) {
//                // wrong index, don't move.
//                return
//            }
//            var scrollDx = 0f
//            var scrollDy = 0f
//            val scrollQueueSize = queuedScrollDx.size
//            for (i in 0 until scrollQueueSize) {
//                scrollDx += queuedScrollDx.poll()
//                scrollDy += queuedScrollDy.poll()
//            }
//
//            if (isVerticalMode) {
//                val anchor = anchors.removeAt(nowSelectedIndex)
//                anchor.detach()
//                setPoseDataToTempArray(getPose(anchor))
//                //                        log(TAG, "point[" + nowSelectedIndex + "] move vertical "+ (scrollDy / viewHeight) + ", tY=" + tempTranslation[1]
//                //                                + ", new tY=" + (tempTranslation[1] += (scrollDy / viewHeight)));
//                tempTranslation[1] += scrollDy / viewHeight
//                anchors.add(nowSelectedIndex,
//                        session.createAnchor(Pose(tempTranslation, tempRotation)))
//            } else {
//                val toX = showingTapPointX.get(nowSelectedIndex) - scrollDx
//                showingTapPointX.removeAt(nowSelectedIndex)
//                showingTapPointX.add(nowSelectedIndex, toX)
//
//                val toY = showingTapPointY.get(nowSelectedIndex) - scrollDy
//                showingTapPointY.removeAt(nowSelectedIndex)
//                showingTapPointY.add(nowSelectedIndex, toY)
//
//                if (anchors.size > nowSelectedIndex) {
//                    val anchor = anchors.removeAt(nowSelectedIndex)
//                    anchor.detach()
//                    // remove duplicated anchor
//                    setPoseDataToTempArray(getPose(anchor))
//                    tempTranslation[0] -= scrollDx / viewWidth
//                    tempTranslation[2] -= scrollDy / viewHeight
//                    anchors.add(nowSelectedIndex,
//                            session.createAnchor(Pose(tempTranslation, tempRotation)))
//                }
//            }
//        } catch (e: NotTrackingException) {
//            e.printStackTrace()
//        }
//
//    }

    private fun gainRenderingLock(): Boolean {
        synchronized(mRenderingLock) {
            while (mRendering) {
                try {
//                    mRenderingLock.wait()
                } catch (e: InterruptedException) {
                    return false
                }

            }
            mRendering = true
        }
        return true
    }

    /**
     * Used to release the lock obtained in a call to [.gainRenderingLock]
     */
    private fun releaseRenderingLock() {
        synchronized(mRenderingLock) {
            mRendering = false
//            mRenderingLock.notifyAll()
        }
    }

    private fun renderFrame() {


        try {
            //            gainRenderingLock();


            // Obtain the current frame from ARSession. When the configuration is set to
            // UpdateMode.BLOCKING (it is by default), this will throttle the rendering to the
            // camera framerate.
            val frame = frame1//session.update();
            val camera = camera1//frame.getCamera();
            // Handle one tap per frame.
            handleTap(frame, camera)

            // If frame is ready, render camera preview image to the GL surface.
            backgroundRenderer.draw(frame)

            // Keep the screen unlocked while tracking, but allow it to lock when tracking stops.
            trackingStateHelper.updateKeepScreenOnFlag(camera.trackingState)

            // If not tracking, don't draw 3D objects, show tracking failure reason instead.
            if (camera.trackingState == TrackingState.PAUSED) {
                //                Toast.makeText(this,TrackingStateHelper.getTrackingFailureReasonString(camera),Toast.LENGTH_SHORT).show();
                Log.e("value", TrackingStateHelper.getTrackingFailureReasonString(camera))

                return
            }

            // Get projection matrix.
            val projmtx = FloatArray(16)
            camera.getProjectionMatrix(projmtx, 0, 0.1f, 100.0f)

            // Get camera matrix and draw.
            val viewmtx = FloatArray(16)
            camera.getViewMatrix(viewmtx, 0)

            // Compute lighting from average intensity of the image.
            // The first three components are color scaling factors.
            // The last one is the average pixel intensity in gamma space.
            val colorCorrectionRgba = FloatArray(4)
            frame.lightEstimate.getColorCorrection(colorCorrectionRgba, 0)

            // Visualize tracked points.
            // Use try-with-resources to automatically release the point cloud.
            /*   try (PointCloud pointCloud = frame.acquirePointCloud()) {
                pointCloudRenderer.update(pointCloud);
                pointCloudRenderer.draw(viewmtx, projmtx);
            }*/

            // No tracking error at this point. If we detected any plane, then hide the
            // message UI, otherwise show searchingPlane message.
            if (hasTrackingPlane()) {
                //                messageSnackbarHelper.hide(this);
            } else {
                //                Toast.makeText(this,SEARCHING_PLANE_MESSAGE,Toast.LENGTH_SHORT).show();
                Log.e("test", SEARCHING_PLANE_MESSAGE)
            }

            // Visualize planes.

            ///            planeRenderer.drawPlanes(cp, camera.getDisplayOrientedPose(), projmtx);

            //            gainRenderingLock();

            // Visualize anchors created by touch.
            val scaleFactor = 1.0f

            for (coloredAnchor in anchors) {
                if (coloredAnchor.anchor.trackingState != TrackingState.TRACKING) {
                    continue
                }
                // Get the current pose of an Anchor in world space. The Anchor pose is updated
                // during calls to session.update() as ARCore refines its estimate of the world.
                coloredAnchor.anchor.pose.toMatrix(anchorMatrix, 0)

                // Update and draw the model and its shadow.
                virtualObject.updateModelMatrix(anchorMatrix, scaleFactor)
                virtualObjectShadow.updateModelMatrix(anchorMatrix, scaleFactor)
                virtualObject.draw(viewmtx, projmtx, colorCorrectionRgba, coloredAnchor.color)
                virtualObjectShadow.draw(viewmtx, projmtx, colorCorrectionRgba, coloredAnchor.color)
            }
            //            long time = SystemClock.uptimeMillis() % 10000L;
            //            float angleInDegrees = (360.0f / 10000.0f) * ((int) time);
            //            setCurrentAngle(angleInDegrees);
            // If streaming, render a 2nd pass to the video encoder
            //            releaseRenderingLock();

        } catch (t: Throwable) {
            // Avoid crashing the application due to unhandled exceptions.
        }

    }

    private fun takeScreenshot() {
        val now = Date()
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now)

        try {
            // image naming and path  to include sd card  appending name you choose for file
            val mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg"

            // create bitmap screen capture
            val v1 = getWindow().getDecorView().getRootView()
            v1.setDrawingCacheEnabled(true)
            val bitmap = Bitmap.createBitmap(v1.getDrawingCache())
            v1.setDrawingCacheEnabled(false)

            val imageFile = File(mPath)

            val outputStream = FileOutputStream(imageFile)
            val quality = 100
            val s = bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)
            if (imageFile.exists()) {
                myBitmap = BitmapFactory.decodeFile(imageFile.absolutePath)
            }
            outputStream.flush()
            outputStream.close()

            uploadFile(myBitmap!!)
        } catch (e: Throwable) {
            // Several error may come out with file handling or DOM
            e.printStackTrace()
        }

    }

    private fun openScreenshot(s: Bitmap) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun getScreenCenter(): android.graphics.Point {
        return android.graphics.Point(50, 50)
    }

    override fun onDestroy() {
        super.onDestroy()
//        firestoreListener!!.remove()
    }

    companion object {
        private val TAG = OpenGLActivity::class.java.simpleName
        private val DEFAULT_COLOR = floatArrayOf(255f, 0f, 0f, 0f)

        private val SEARCHING_PLANE_MESSAGE = "Searching for surfaces..."
    }
}
