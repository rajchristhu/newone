package com.wowza.gocoder.sdk.sampleapp

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.osw.osw_customer.data.RepositoryCallback

open class BaseViewModel : ViewModel() {
    var _showErrorSnackBar: MutableLiveData<Int> = MutableLiveData()


    fun checkInvalidError(error: RepositoryCallback.Error, invalidError: String, errorCode: String): Boolean {
        if (invalidError == "OK") {
            if (error == RepositoryCallback.Error.DATA) {
                _showErrorSnackBar.value = R.string.data_error_snack_bar
            } else if (error == RepositoryCallback.Error.NETWORK) {
                _showErrorSnackBar.value = R.string.network_error_snack_bar
            }

            return true
        }

        return false
    }
}