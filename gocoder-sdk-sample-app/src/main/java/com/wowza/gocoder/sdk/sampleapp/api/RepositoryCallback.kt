package com.osw.osw_customer.data

interface RepositoryCallback<M> {

    fun onSuccess(model: M)

    fun onFailure(error: Error, invalidError: String, errorCode: String)

    enum class Error {
        DATA, NETWORK
    }

}
