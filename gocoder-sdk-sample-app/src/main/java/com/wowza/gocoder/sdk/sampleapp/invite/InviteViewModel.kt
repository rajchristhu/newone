package com.wowza.gocoder.sdk.sampleapp.invite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class InviteViewModel : ViewModel() {

    var inviteListResponse:MutableLiveData<List<String>> = MutableLiveData()

    fun inviteList(){
        val intviteUserList: MutableList<String> = mutableListOf()
        intviteUserList.add("Mariya")
        intviteUserList.add("Christhu")
        intviteUserList.add("Israel")
        intviteUserList.add("Prabu")
        intviteUserList.add("Raj")
        intviteUserList.add("Kumar")
        intviteUserList.add("Praveen")
        inviteListResponse.value = intviteUserList

    }
}