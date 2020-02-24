package com.example.contrivedexample.nonRx

import com.example.contrivedexample.shared.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface SampleApi {

    @GET("users/")
    fun getUsers() : Call<List<User>>

    @GET("users/{profileId}")
    fun getUserProfile(@Path("profileId") userId: String) : Call<User>

}