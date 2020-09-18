package com.example.underpressurea.common

import com.example.underpressurea.remote.FirebaseInterface
import com.example.underpressurea.remote.IGoogleAPIService
import com.example.underpressurea.remote.RetrofitClient
import com.example.underpressurea.remote.RetrofitPOSTClient

object Common {
    private val GOOGLE_API_URL ="https://maps.googleapis.com/"
    private val MY_SERVER_API_URL ="https://todo-ilwhnrepnq-uc.a.run.app/"

    val googleApiService: IGoogleAPIService
        get()=RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)

    val firebaseInterfaceService: FirebaseInterface
        get()=RetrofitPOSTClient.getClient(MY_SERVER_API_URL).create(FirebaseInterface::class.java)
}