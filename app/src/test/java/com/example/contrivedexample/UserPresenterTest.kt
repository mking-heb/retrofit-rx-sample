package com.example.contrivedexample

import com.example.contrivedexample.nonRx.SampleApi
import com.example.contrivedexample.nonRx.UserPresenter
import com.example.contrivedexample.shared.User
import com.example.contrivedexample.shared.UserView
import com.nhaarman.mockitokotlin2.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@RunWith(MockitoJUnitRunner::class)
class UserPresenterTest {

    @Mock
    lateinit var api: SampleApi
    @Mock
    lateinit var view: UserView
    lateinit var presenter: UserPresenter

    @Before
    fun setup() {
        presenter = UserPresenter(api, view)
    }

    @Test
    fun `get users success shows users`() {
        val mockCall : Call<List<User>> = mock()
        val callbackCaptor = argumentCaptor<Callback<List<User>>>()
        whenever(api.getUsers()).thenReturn(mockCall)
        presenter.loadUsers()

        //ensure API is called
        verify(mockCall).enqueue(callbackCaptor.capture())

        //setup behavior on API response
        val users = listOf(
            User("12345", "Molly", "green")
        )
        val mockResponse: Response<List<User>> = mock()
        whenever(mockResponse.isSuccessful).thenReturn(true)
        whenever(mockResponse.body()).thenReturn(users)

        //call API response
        callbackCaptor.firstValue.onResponse(mockCall, mockResponse)

        //verify behavior
        verify(view).showUsers(users)
    }

    @Test
    fun `get users failure shows error`() {
        val mockCall : Call<List<User>> = mock()
        val callbackCaptor = argumentCaptor<Callback<List<User>>>()
        whenever(api.getUsers()).thenReturn(mockCall)
        presenter.loadUsers()

        //ensure API is called
        verify(mockCall).enqueue(callbackCaptor.capture())

        //setup behavior on API response
        val error = Throwable()

        //call API response
        callbackCaptor.firstValue.onFailure(mockCall, error)

        //verify behavior
        verify(view).showError()
    }

    @Test
    fun `get users non 200 response shows error`() {
        val mockCall : Call<List<User>> = mock()
        val callbackCaptor = argumentCaptor<Callback<List<User>>>()
        whenever(api.getUsers()).thenReturn(mockCall)
        presenter.loadUsers()

        //ensure API is called
        verify(mockCall).enqueue(callbackCaptor.capture())

        //setup behavior on API response
        val mockResponse: Response<List<User>> = mock()
        whenever(mockResponse.isSuccessful).thenReturn(false)

        //call API response
        callbackCaptor.firstValue.onResponse(mockCall, mockResponse)

        //verify behavior
        verify(view).showError()
    }

    @Test
    fun `get users response with empty body shows error`() {
        val mockCall : Call<List<User>> = mock()
        val callbackCaptor = argumentCaptor<Callback<List<User>>>()
        whenever(api.getUsers()).thenReturn(mockCall)
        presenter.loadUsers()

        //ensure API is called
        verify(mockCall).enqueue(callbackCaptor.capture())

        //setup behavior on API response
        val mockResponse: Response<List<User>> = mock()
        whenever(mockResponse.isSuccessful).thenReturn(true)
        whenever(mockResponse.body()).thenReturn(null)

        //call API response
        callbackCaptor.firstValue.onResponse(mockCall, mockResponse)

        //verify behavior
        verify(view).showError()
    }

    @Test
    fun `select user calls API with correct value`() {
        val idCaptor = argumentCaptor<String>()
        val callbackCaptor = argumentCaptor<Callback<User>>()
        val mockCall : Call<User> = mock()
        whenever(api.getUserProfile(idCaptor.capture())).thenReturn(mockCall)
        presenter.onUserSelected("12345")

        // ensure ID was correctly changed before passed to API
        assert(idCaptor.firstValue == "012345")
        verify(mockCall).enqueue(callbackCaptor.capture())
    }

    @Test
    fun `select user happy path calls view show details`() {
        val callbackCaptor = argumentCaptor<Callback<User>>()
        val mockCall : Call<User> = mock()
        whenever(api.getUserProfile(any())).thenReturn(mockCall)
        presenter.onUserSelected("12345")

        //verify call is made
        verify(mockCall).enqueue(callbackCaptor.capture())

        // verify behavior on response
        val user =
            User("12345", "Molly", "green")
        val mockResponse : Response<User> = mock()
        whenever(mockResponse.isSuccessful).thenReturn(true)
        whenever(mockResponse.body()).thenReturn(user)

        callbackCaptor.firstValue.onResponse(mockCall, mockResponse)

        verify(view).showUserDetails(user)
    }

    @Test
    fun `select user failure path calls view show error`() {
        val callbackCaptor = argumentCaptor<Callback<User>>()
        val mockCall : Call<User> = mock()
        whenever(api.getUserProfile(any())).thenReturn(mockCall)
        presenter.onUserSelected("12345")

        //verify call is made
        verify(mockCall).enqueue(callbackCaptor.capture())

        // verify behavior on response
        val mockResponse : Response<User> = mock()
        whenever(mockResponse.isSuccessful).thenReturn(false)

        callbackCaptor.firstValue.onResponse(mockCall, mockResponse)

        verify(view).showError()
    }

    @Test
    fun `select user error path calls view show error`() {
        val callbackCaptor = argumentCaptor<Callback<User>>()
        val mockCall : Call<User> = mock()
        whenever(api.getUserProfile(any())).thenReturn(mockCall)
        presenter.onUserSelected("12345")

        //verify call is made
        verify(mockCall).enqueue(callbackCaptor.capture())

        // verify behavior on response
        val mockResponse : Response<User> = mock()
        whenever(mockResponse.isSuccessful).thenReturn(false)

        callbackCaptor.firstValue.onResponse(mockCall, mockResponse)

        verify(view).showError()
    }

    @Test
    fun `select user success but null calls view show error`() {
        val callbackCaptor = argumentCaptor<Callback<User>>()
        val mockCall : Call<User> = mock()
        whenever(api.getUserProfile(any())).thenReturn(mockCall)
        presenter.onUserSelected("12345")

        //verify call is made
        verify(mockCall).enqueue(callbackCaptor.capture())

        // verify behavior on response
        val mockResponse : Response<User> = mock()
        whenever(mockResponse.isSuccessful).thenReturn(true)
        whenever(mockResponse.body()).thenReturn(null)

        callbackCaptor.firstValue.onResponse(mockCall, mockResponse)

        verify(view).showError()
    }
}