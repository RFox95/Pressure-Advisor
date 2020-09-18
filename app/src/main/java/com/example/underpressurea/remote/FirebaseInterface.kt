package com.example.underpressurea.remote

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface FirebaseInterface {
    @Headers("Content-Type: application/json")
    @POST("add")
    fun addPressure(@Body body: String): Call<ResponseBody>
}