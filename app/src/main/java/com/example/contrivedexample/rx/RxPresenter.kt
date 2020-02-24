package com.example.contrivedexample.rx

import com.example.contrivedexample.shared.UserView
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class RxPresenter (val api: RxApi, val view: UserView, val scheduler: SchedulerProvider) {

    private val compositeDisposable = CompositeDisposable()

    fun loadUsers() {
        compositeDisposable.add(
        api.getUsers()
            .subscribeOn(scheduler.subscribeOn())
            .observeOn(scheduler.observeOn())
            .subscribe( {
                view.showUsers(it)
            },{
                view.showError()
            }))
    }

    fun onUserSelected(userId: String) {
        val apiUserId = "0$userId"
        compositeDisposable.add(api.getUserProfile(apiUserId)
            .subscribeOn(scheduler.subscribeOn())
            .observeOn(scheduler.observeOn())
            .subscribe ({
                view.showUserDetails(it)
            }, {
                view.showError()
            }))
    }

    fun onStop() {
        compositeDisposable.clear()
    }
}

class RealScheduler : SchedulerProvider {
    override fun subscribeOn(): Scheduler {
        return Schedulers.io()
    }

    override fun observeOn(): Scheduler {
        return AndroidSchedulers.mainThread()
    }
}