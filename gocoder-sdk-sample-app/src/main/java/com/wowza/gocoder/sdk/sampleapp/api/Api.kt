package com.osw.osw_customer.data.api


import com.focuzar.holoassist.api.request.LoginResponce
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface Api {


    @POST("xassist/otp/otp.php")
    fun vendorMutiPackage(@Query("to") id: String): Call<LoginResponce>



}
