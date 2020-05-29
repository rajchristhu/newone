package com.osw.osw_customer.data



import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.focuzar.holoassist.api.request.LoginResponce
import com.focuzar.holoassist.utilities.SessionMaintainence
import com.osw.osw_customer.data.api.Api
import com.osw.osw_customer.data.api.ApiCallback
import com.osw.osw_customer.data.api.ApiClient
import okhttp3.*
import org.jetbrains.anko.doAsync
import retrofit2.Call
import retrofit2.Response
import java.io.File


class Repository private constructor() {

    private val api: Api

    init {
        api = ApiClient.instance.api
    }


    fun Login(origin: String): MutableLiveData<String> {



            val call =api.vendorMutiPackage(origin)
            var data = MutableLiveData<String>()
            doAsync {
                call.enqueue(object :  retrofit2.Callback<LoginResponce> {
                    override fun onFailure(call: Call<LoginResponce>, t: Throwable) {
Log.e("Dafdf",t.toString())                    }

                    override fun onResponse(
                        call: Call<LoginResponce>,
                        response: Response<LoginResponce>
                    ) {
                        data.value = response!!.body()!!.otp!!
                        SessionMaintainence.instance!!.pin=response!!.body()!!.otp!!
                    }


                })
            }

            return data


//        api.vendorMutiPackage(origin).enqueue(ApiCallback(callback))
    }

    companion object {

        val instance = Repository()
    }
}
