package com.example.contrivedexample.rx

import com.example.contrivedexample.shared.User
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Path

interface RxApi {

    @GET("users/")
    fun getUsers() : Observable<List<User>>

    @GET("users/{profileId}")
    fun getUserProfile(@Path("profileId") userId: String) : Observable<User>

}