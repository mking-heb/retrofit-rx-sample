package com.example.contrivedexample

import com.example.contrivedexample.rx.RxApi
import com.example.contrivedexample.rx.RxPresenter
import com.example.contrivedexample.rx.SchedulerProvider
import com.example.contrivedexample.shared.User
import com.example.contrivedexample.shared.UserView
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.argumentCaptor
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class RxPresenterTest {

    @Mock
    lateinit var api: RxApi

    @Mock
    lateinit var view: UserView

    lateinit var presenter: RxPresenter

    val scheduler= object : SchedulerProvider {
        override fun subscribeOn(): Scheduler {
            return Schedulers.trampoline()
        }

        override fun observeOn(): Scheduler {
            return Schedulers.trampoline()
        }
    }

    @Before
    fun setup() {
        presenter = RxPresenter(api, view, scheduler)
    }

    @Test
    fun `get users success shows users`() {
        val users = listOf(
            User("12345", "Molly", "green")
        )
        whenever(api.getUsers()).thenReturn(Observable.just(users))

        presenter.loadUsers()

        verify(view).showUsers(users)
    }

    @Test
    fun `get users failure shows error`() {
        whenever(api.getUsers()).thenReturn(Observable.error(Throwable()))

        presenter.loadUsers()

        verify(view).showError()
    }

    @Test
    fun `select user calls API with correct value and shows details`() {
        val user =
            User("12345", "Molly", "green")
        val argumentCaptor = argumentCaptor<String>()
        whenever(api.getUserProfile(any())).thenReturn(Observable.just(user))

        presenter.onUserSelected("12345")

        verify(api).getUserProfile(argumentCaptor.capture())

        assert(argumentCaptor.firstValue.equals("012345"))

        verify(view).showUserDetails(user)
    }

    @Test
    fun `select user failure calls showError`() {
        val argumentCaptor = argumentCaptor<String>()
        whenever(api.getUserProfile(any())).thenReturn(Observable.error(Throwable()))

        presenter.onUserSelected("12345")

        verify(api).getUserProfile(argumentCaptor.capture())

        assert(argumentCaptor.firstValue.equals("012345"))

        verify(view).showError()
    }

    // IF we wanted to do something different for particular error cases (such as a 401 response),
    // we would want to test for that as well
//    @Test
//    fun `get users 401 shows reauth`() {
//        val errorResponse: Response<List<User>> = Response.error(401,
//            ResponseBody.create(MediaType.parse("application/json"), "{}"))
//        whenever(api.getUsers()).thenReturn(Observable.error(HttpException(errorResponse)))
//
//        presenter.loadUsers()
//
//        verify(view).showReauth()
//    }
}