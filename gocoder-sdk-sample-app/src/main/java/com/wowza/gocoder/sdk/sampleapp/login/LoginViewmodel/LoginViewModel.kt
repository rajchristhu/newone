package com.wowza.gocoder.sdk.sampleapp.login.LoginViewmodel

import androidx.lifecycle.MutableLiveData
import com.wowza.gocoder.sdk.sampleapp.BaseViewModel
import com.osw.osw_customer.data.Repository

class LoginViewModel : BaseViewModel() {
    var _loginResponse: MutableLiveData<String> = MutableLiveData()
    fun doLogin(userName: String) {

        _loginResponse = Repository.instance.Login(userName)
    }
}