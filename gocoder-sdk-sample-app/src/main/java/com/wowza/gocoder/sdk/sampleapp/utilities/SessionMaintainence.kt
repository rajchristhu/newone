package com.focuzar.holoassist.utilities


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences


import java.util.ArrayList
import java.util.HashMap

class SessionMaintainence
/**
 * This is the parmeterized constructor.
 *
 * @param context This is the parameter used to pass the context of an activity.
 */
private constructor(context: Context) {
    private var userpreferences: SharedPreferences? = null
    private var courseID: Int? = null
    private var preferences: SharedPreferences
    /**
     * The Editor.
     */
    private var editor: SharedPreferences.Editor
    /**
     * The Context.
     */
    internal var context: Context

    /**
     * This method is used to read an activity screen name.
     *
     * @return This returns an activity screen name.
     */
    var termsandcon: Boolean
        get() = preferences.getBoolean(TERMANDCON, false)
        set(termsandcon) {
            editor.putBoolean(TERMANDCON, termsandcon)
            editor.commit()
        }
    var referralpagecheck: Boolean
        get() = preferences.getBoolean(REFERRAL, false)
        set(referralpagecheck) {
            editor.putBoolean(REFERRAL, referralpagecheck)
            editor.commit()
        }
    var referralcheck: Boolean
        get() = preferences.getBoolean(REFERRALs, false)
        set(referralcheck) {
            editor.putBoolean(REFERRALs, referralcheck)
            editor.commit()
        }
    var profilepage: Boolean
        get() = preferences.getBoolean(profile, false)
        set(termsandcon) {
            editor.putBoolean(profile, termsandcon)
            editor.commit()
        }

    /**
     * This method is used to insert an activity screen name.
     *
     * @param screen_name This is the parameter used to pass an activity screen name.
     */
    var phoneno: String?
        get() = preferences.getString(KEY_PAGE, "")
        set(phoneno) {
            editor.putString(KEY_PAGE, phoneno)
            editor.commit()
        }
    var addressverify: String?
        get() = preferences.getString(ADDRESS_VERIFY, "")
        set(addressverify) {
            editor.putString(ADDRESS_VERIFY, addressverify)
            editor.commit()
        }
    var vidverify: String?
        get() = preferences.getString(VIDEO_VERIFY, "")
        set(vidverify) {
            editor.putString(VIDEO_VERIFY, vidverify)
            editor.commit()
        }
    var vidverifys: String?
        get() = preferences.getString(VIDEO_VERIFYS, "")
        set(vidverifys) {
            editor.putString(VIDEO_VERIFYS, vidverifys)
            editor.commit()
        }
    var photoverify: String?
        get() = preferences.getString(PHOTO_VERIFY, "")
        set(photoverify) {
            editor.putString(PHOTO_VERIFY, photoverify)
            editor.commit()
        }
    var frt: String?
        get() = preferences.getString(FRT, "")
        set(frt) {
            editor.putString(FRT, frt)
            editor.commit()
        }
    var imj: String?
        get() = preferences.getString(LATS, "")
        set(imj) {
            editor.putString(LATS, imj)
            editor.commit()
        }


    var vidvers: String?
        get() = preferences.getString(VID, "")
        set(vidver) {
            editor.putString(VID, vidver)
            editor.commit()
        }
    var lat: String?
        get() = preferences.getString(LAT, "")
        set(lat) {
            editor.putString(LAT, lat)
            editor.commit()
        }
    var long: String?
        get() = preferences.getString(LONG, "")
        set(long) {
            editor.putString(LONG, long)
            editor.commit()
        }
//    var cat: ArrayList<String>?
//        get() = preferences.getStringSet(CAT, "")
//        set(cat) {
//            editor.putString(cat, cat)
//            editor.commit()
//        }


    var firstName: String?
        get() = preferences.getString(FIRST, "")
        set(firstName) {
            editor.putString(FIRST, firstName)
            editor.commit()
        }

    var address: String?
        get() = preferences.getString(ADD, "")
        set(address) {
            editor.putString(ADD, address)
            editor.commit()
        }
    var fullname: String?
        get() = preferences.getString(FULL, "")
        set(fullname) {
            editor.putString(FULL, fullname)
            editor.commit()
        }
    var userType: String?
        get() = preferences.getString(userTypes, "")
        set(userType) {
            editor.putString(userTypes, userType)
            editor.commit()
        }


    /**
     * Is loggedin boolean.
     *
     * @return the boolean
     */
    /**
     * Sets loggedin.
     *
     * @param login the login
     */
    var is_loggedin: Boolean
        get() = preferences.getBoolean(IS_LOGIN, false)
        set(login) {
            editor.putBoolean(IS_LOGIN, login)
            if (!login) {
                userpreferences = null
            }
            editor.commit()
        }

    /**
     * Is first loggedin boolean.
     *
     * @return the boolean
     */
    val is_first_loggedin: Boolean
        get() = preferences.getBoolean(IS_FIRST_LOGIN, false)

    /**
     * Is first install boolean.
     *
     * @return the boolean
     */
    val is_first_install: Boolean
        get() = preferences.getBoolean(IS_FIRST_INSTALL, true)


    /**
     * Gets routed activity.
     *
     * @return the routed activity
     */
    val routedActivity: String?
        get() = preferences.getString(KEY_ACTIVITY_NAME, "")

    val courseId: Int?
        get() = preferences.getInt(KEY_COURSE_ID, 0)

    var courseName: String?
        get() = preferences.getString(KEY_COURSE_NAME, "")
        set(coursename) {
            editor.putString(KEY_COURSE_NAME, coursename)
            editor.commit()
        }

    var emailname: String?
        get() = preferences.getString(KEY_EMAIL, "")
        set(emailname) {
            editor.putString(KEY_EMAIL, emailname)
            editor.commit()
        }


    val gcmToken: String?
        get() = preferences.getString(KEY_GCM_TOKEN, "")

    var caseStudyToken: String?
        get() = preferences.getString(KEY_CASESTUDY_TOKEN, "")
        set(token) {

            editor.putString(KEY_CASESTUDY_TOKEN, token)
            editor.commit()
        }

    var userid: String?
        get() = preferences.getString(USERID, "")
        set(userId) {

            editor.putString(USERID, userId)
            editor.commit()
        }

    var formCount: String?
        get() = preferences.getString(COUNT, "0")
        set(count) {

            editor.putString(COUNT, count)
            editor.commit()
        }
    var namesw: String?
        get() = preferences.getString(NAAS, "")
        set(namesw) {

            editor.putString(NAAS, namesw)
            editor.commit()
        }
    var passw: String?
        get() = preferences.getString(TYPES, "")
        set(passw) {
            editor.putString(TYPES, passw)
            editor.commit()
        }

    var courseJson: String?
        get() = preferences.getString(KEY_COURSE_JSON, "")
        set(courseJson) {

            editor.putString(KEY_COURSE_JSON, courseJson)
            editor.commit()
        }

    var pin: String?
        get() = preferences.getString(Pin, "")
        set(pin) {
            editor.putString(Pin, pin)
            editor.commit()
        }

    var Uid: String?
        get() = preferences.getString(UserId, "")
        set(Uid) {
            editor.putString(UserId, Uid)
            editor.commit()
        }


    val courseInfo: Boolean
        get() = preferences.getBoolean(KEY_ISCOURSEINFO, false)

    var firstn: String?
        get() = preferences.getString(KEY_LANG, "")
        set(firstn) {
            editor.putString(KEY_LANG, firstn)
            editor.commit()
        }

    var lastname: String?
        get() = preferences.getString(KEY_ASS, "")
        set(lastname) {
            editor.putString(KEY_ASS, lastname)
            editor.commit()
        }

    init {
        this.context = context
        preferences = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = preferences.edit()

    }

    /**
     * This method is used to clear the sessions.
     */
    fun clearSession() {
        editor.clear()
        editor.commit()
    }

    /**
     * Sets is first loggedin.
     *
     * @param login the login
     */
    fun set_is_first_loggedin(login: Boolean) {
        editor.putBoolean(IS_FIRST_LOGIN, login)
        editor.commit()
    }


    /**
     * Sets is first installn.
     *
     * @param login the login
     */
    fun set_is_first_installn(login: Boolean) {
        editor.putBoolean(IS_FIRST_INSTALL, login)
        editor.commit()
    }

    fun set_availablepincode(pincode: Boolean) {
        editor.putBoolean("availablepincode", pincode)
        editor.commit()
    }

    var set_availablepincode: Boolean
        get() = preferences.getBoolean("availablepincode", false)
        set(pincode) {
            editor.putBoolean("availablepincode", pincode)
            if (!pincode) {
                userpreferences = null
            }
            editor.commit()
        }

    /**
     * Gets user id.
     *
     * @return the user id
     */
    fun getUserId(): String? {
        return preferences.getString(KEY_USER_ID, "")
    }


    /**
     * Activity routed from.
     *
     * @param activityName the activity name
     */
    fun activityRoutedFrom(activityName: String) {
        editor.putString(KEY_ACTIVITY_NAME, activityName)
        editor.commit()
    }

    fun setCourseID(courseID: Int?) {
        this.courseID = courseID

        editor.putInt(KEY_COURSE_ID, courseID!!)
        editor.commit()
    }


    val role: String?
        get() = preferences.getString(KEY_ROLE, "")

    fun setRole(role: String) {
        editor.putString(KEY_ROLE, role)
        editor.commit()
    }

    fun setgcmToken(gcmToken: String) {
        editor.putString(KEY_GCM_TOKEN, gcmToken)
        editor.commit()
    }


    fun setTotalPage(totalPage: Int) {
        editor.putInt(KEY_TOTAL_PAGES, totalPage)
        editor.commit()
    }


    fun isCourseInfo(b: Boolean) {
        editor.putBoolean(KEY_ISCOURSEINFO, b)
        editor.commit()
    }




    companion object {
        /**
         * The constant PREF_NAME.
         */
        val PREF_NAME = "quickFix"
        /**
         * The constant IS_LOGIN.
         */
        var IS_LOGIN = "login"
        /**
         * The constant IS_FIRST_LOGIN.
         */
        val IS_FIRST_LOGIN = "first_login"
        /**
         * The constant KEY_USER_CREDENTIALS.
         */
        val KEY_USER_CREDENTIALS = "user_crendtial"
        val USERID = "user_id"
        val PUBLIC_KEY = "2150653c51"
        val PRIVATE_KEY = "cd957525351b973"
        val ADD = "add"
        private val KEY_PAGE = "screen_name"
        private val TERMANDCON = "termsandcon"
        private val REFERRAL = "referral"
        private val REFERRALs = "referrals"
        private val type = ""
        private val VID = ""
        private val availablepincode = 0
        private val IS_FIRST_INSTALL = "first_install"
        private val KEY_USER = "user"
        private val KEY_LAST_SYNC_TIME = "last_sync_time"
        private val KEY_PRE_SURVEY_STATUS = "pre_survey"
        private val KEY_POST_SURVEY_STATUS = "post_survey"
        private val KEY_MODULE_STATUS = "module_Status"
        private val KEY_APP_GUID = "app_guid"
        private val KEY_SESSION_ID = "app_session_guid"
        private val KEY_CERTIFICATE_STATUS = "certificate_status"
        private val KEY_TRACK_EVENT = "track_event"
        private val KEY_CERTIFICATE_IMAGE = "certificate_image"
        private val KEY_USER_ID = "unique_user_id"
        private val USER_PREF_PREFIX = "userid_"
        private val KEY_ACTIVITY_NAME = "activity_name"
        private val KEY_ACCESSTOKEN = "access_token"
        private val ADDRESS_VERIFY = "address_verify"
        private val VIDEO_VERIFY = "video_verify"
        private val VIDEO_VERIFYS = "video_verifyS"
        private val PHOTO_VERIFY = "photo_verify"
        private val KEY_COURSE_ID = "course_id"
        private val FULL = "fullname"
        private val UserId = ""
        private val KEY_COURSE_NAME = "course_name"
        private val KEY_COURSE_STATUS = "course_status"
        private val FIRST = "first"
        private val Pin = "Pin"
        private val LAT = "lat"
        private val LATS = "lats"
        private val LONG = "long"
        private val KEY_EMAIL = "emailid"
        private val KEY_PROFILE_DETAILS = "profile_details"
        private val profile = "profile"
        private val KEY_ROLE = "role"
        private val KEY_GCM_TOKEN = "gcm_token"
        private val KEY_TOTAL_PAGES = ""
        private val UID = ""
        private val KEY_TAGS = "tagss"
        private val KEY_COURSE_JSON = "course_json"
        private val KEY_CASESTUDY_TOKEN = "casestudy_token"
        private val KEY_COUNTRY = "country"
        private val KEY_ISCOURSEINFO = "course_info"
        private val KEY_LANG = "language"
        private val FRT = "frt"
        private val KEY_ASS = "ass_ans"
        private val userTypes = "user"
        var COUNT = "count"
        var NAAS = "csc"
        var TYPES = "sdw"
        var gravityUrl: String? = null
        var convertedStr = ""
        var preFormId = ""
        var postFormId = ""
        var formId = ""
        var assessId = "0"
        var millis: Long = 0
        var mandList: List<String> = ArrayList()
        var ansId = ""
        var ansVal = ""

        var map: Map<String, List<String>> = HashMap()
        var assessMap: Map<String, List<String>> = HashMap()

        /**
         * The Preferences.
         */

        /**
         * The Private mode.
         */
        internal var PRIVATE_MODE = 0
        /**
         * Gets instance.
         *
         * @return the instance
         */
        @SuppressLint("StaticFieldLeak")
        var instance: SessionMaintainence? = null
            private set
        private val userId = ""
        var assessquiz_Id = "0"
        var testquiz_Id = "0"
        var testId = "0"
        var gravityformId = "0"

        /**
         * Init.
         *
         * @param appContext the app context
         */
        fun init(appContext: Context) {
            if (instance == null) {
                instance = SessionMaintainence(appContext)

            }
        }
    }
}
