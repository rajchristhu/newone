package com.osw.osw_customer.data.api

import com.google.gson.annotations.SerializedName

data class ApiCommonResponse(


        @field:SerializedName("requestId")
        val requestId: String? = null,

        @field:SerializedName("status")
        val status: Int? = null
)