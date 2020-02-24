package com.example.contrivedexample.nonRx

import com.example.contrivedexample.shared.User
import com.example.contrivedexample.shared.UserView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPresenter(val api: SampleApi, val view: UserView) {

    fun loadUsers() {
        api.getUsers().enqueue(object : Callback<List<User>> {
            override fun onFailure(call: Call<List<User>>, t: Throwable) {
                view.showError()
            }

            override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                if(response.isSuccessful) {
                    response.body()?.let {
                        view.showUsers(it)
                    } ?: view.showError()
                } else {
                    view.showError()
                }
            }
        })
    }

    fun onUserSelected(userId: String) {
        val apiUserId = "0$userId"
        api.getUserProfile(apiUserId).enqueue(object : Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                view.showError()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {
                if(response.isSuccessful) {
                    response.body()?.let {
                        view.showUserDetails(it)
                    } ?: view.showError()
                } else {
                    view.showError()
                }
            }
        })
    }
}