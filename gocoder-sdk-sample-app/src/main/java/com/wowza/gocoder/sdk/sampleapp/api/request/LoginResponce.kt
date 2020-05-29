package com.focuzar.holoassist.api.request

import com.google.gson.annotations.SerializedName

class LoginResponce {

    @SerializedName("status_id")
    var status_id: String? = null
    @SerializedName("status_message")
    var status_message: String? = null
    @SerializedName("otp")
    var otp: String? = null
}