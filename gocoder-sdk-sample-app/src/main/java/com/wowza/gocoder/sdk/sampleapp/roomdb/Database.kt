package com.focuzar.holoassist.roomdb

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = arrayOf(MessageUser::class), version = 1)
abstract class Database : RoomDatabase() {

    abstract fun messageDao(): MessageDao
}