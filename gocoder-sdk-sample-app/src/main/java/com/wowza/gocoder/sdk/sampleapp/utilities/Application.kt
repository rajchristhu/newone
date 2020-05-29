package com.wowza.gocoder.sdk.sampleapp.utilities

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.focuzar.holoassist.roomdb.Database
import com.focuzar.holoassist.roomdb.extensions.objectOf
import com.focuzar.holoassist.utilities.SessionMaintainence


class Application : Application() {
    var context: Context? = null
    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(this, objectOf<Database>(), "chat.db").build()
        SessionMaintainence.init(this)
        context = applicationContext

    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
//        MultiDex.install(this)
    }
    companion object {
        lateinit var database: Database
    }
}
