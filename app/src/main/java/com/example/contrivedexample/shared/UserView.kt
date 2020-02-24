package com.example.contrivedexample.shared

interface UserView {
    fun showError()
    fun showUsers(users: List<User>)
    fun showUserDetails(user: User)
}