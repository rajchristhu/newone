package com.focuzar.holoassist.roomdb.extensions

internal inline fun <reified T : Any> objectOf() = T::class.java

internal fun getId(): Long = Math.random().toLong()