package com.example.contrivedexample.rx

import io.reactivex.Scheduler

interface SchedulerProvider {
    fun subscribeOn(): Scheduler
    fun observeOn(): Scheduler
}