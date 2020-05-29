package com.osw.osw_customer.data.api


import com.osw.osw_customer.data.RepositoryCallback
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.util.*

class ApiCallback<M>(private val repositoryCallback: RepositoryCallback<M>) : Callback<M> {

    override fun onResponse(call: Call<M>, response: Response<M>) {
        if (response.isSuccessful) {
            val responseBody = Objects.requireNonNull<M>(response.body())
            //log only for qa. it will slow down app
//            if (BuildConfig.BASE_URL.contains("qa")) {
//                Timber.e(response.body()!!.toString())
//            }
            repositoryCallback.onSuccess(responseBody)
        } else {
            try {
                val result = JSONObject(Objects.requireNonNull<ResponseBody>(response.errorBody()).string())
                /*val url= response.raw().request().url().url().toString()
                val authenticate=!url.contains("authenticate")
                val register=!url.contains("register")
                if( authenticate && register ) {
                    if (result.has("status")) {
                        val status = result.getInt("status")
                        if (status == 400) {
                            var msg = "Server error! Please login again!"
                            if (result.has("title")) {
                                msg = result.getString("title")
                            }
                            EventBus.getDefault().post(ForceLogout(msg))
                        }
                    }
                }*/
                var errorCode = "0"
                if (result.has("errorCode")) {
                    try {
                        errorCode = result.getString("errorCode")
                    } catch (ex: Exception) {
                        errorCode = "0"
                    }
                }
                if (result.has("error_description")) {
                    repositoryCallback.onFailure(RepositoryCallback.Error.DATA, result.getString("error_description"), errorCode)
                } else if (result.has("Message")) {
                    repositoryCallback.onFailure(RepositoryCallback.Error.DATA, result.getString("Message"), errorCode)
                } else if (result.has("title")) {
                    val errorDetail = result.getString("title")
                    if (errorDetail.contains("Unauthorized")) {
                        val msg = "Authentication error! Please login again!"
//                        EventBus.getDefault().post(ForceLogout(msg))
                    } else
                        repositoryCallback.onFailure(RepositoryCallback.Error.DATA, result.getString("title"), errorCode)
                } else if (result.has("serviceName")) {
                    repositoryCallback.onFailure(RepositoryCallback.Error.DATA, result.getString("serviceName"), errorCode)
                } else {
                    repositoryCallback.onFailure(RepositoryCallback.Error.DATA, "Server Error!", errorCode)
                }

            } catch (e: JSONException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

    override fun onFailure(call: Call<M>, t: Throwable) {
        val errorCode = "0"
        if (t is ConnectException) {
            repositoryCallback.onFailure(RepositoryCallback.Error.NETWORK, "OK", errorCode)
        } else {
            repositoryCallback.onFailure(RepositoryCallback.Error.DATA, "OK", errorCode)
        }
    }
}


