package com.osw.osw_customer.data.api


import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


class ApiClient private constructor() {

    private val retrofit: Retrofit
//    private val googleretrofit: Retrofit

    val api: Api
        get() = retrofit.create(Api::class.java)

//    val googleApi: Api
//        get() = googleretrofit.create(Api::class.java)

    init {
        val builder = Retrofit.Builder()

        builder.client(OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY))
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build())

        retrofit = builder
                .baseUrl("http://15.206.149.177/")
                .addConverterFactory(GsonConverterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
//
//        googleretrofit = builder
//                .baseUrl(IMAGE_GOOGLE_BASE_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
//                .build()
    }


    companion object {

        val instance = ApiClient()
    }

}
