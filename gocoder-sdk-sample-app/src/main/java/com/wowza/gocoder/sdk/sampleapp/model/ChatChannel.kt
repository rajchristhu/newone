package com.wowza.gocoder.sdk.sampleapp.model


data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}