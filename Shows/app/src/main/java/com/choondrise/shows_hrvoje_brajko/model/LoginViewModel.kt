package com.choondrise.shows_hrvoje_brajko.model

import android.app.Application
import android.content.Context
import androidx.lifecycle.*
import com.choondrise.shows_hrvoje_brajko.databinding.FragmentLoginBinding
import com.choondrise.shows_hrvoje_brajko.models.LoginRequest
import com.choondrise.shows_hrvoje_brajko.models.LoginResponse
import com.choondrise.shows_hrvoje_brajko.networking.ApiModule
import com.choondrise.shows_hrvoje_brajko.ui.LoginFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val loginResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getLoginResultLiveData(): LiveData<Boolean> {
        return loginResultLiveData
    }

    fun login(email: String, password: String) {
        ApiModule.retrofit.login(LoginRequest(email, password))
            .enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    loginResultLiveData.value = response.isSuccessful
                    val prefs = getApplication<Application>().getSharedPreferences("LOGIN_CREDENTIALS", Context.MODE_PRIVATE) ?: return
                    with(prefs.edit()) {
                        putString("CLIENT", response.headers()["client"])
                        putString("ACCESS_TOKEN", response.headers()["access-token"])
                        apply()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    loginResultLiveData.value = false
                }
            })
    }

    fun validateEmail(email: String, binding: FragmentLoginBinding): Boolean {
        val regex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)".toRegex()
        return if (email.isEmpty()) {
            binding.emailInput.isErrorEnabled = true
            binding.emailInput.error = "Email needs to contain at least 1 character"
            false
        } else if (!email.matches(regex)) {
            binding.emailInput.isErrorEnabled = true
            binding.emailInput.error = "Email does not match email regex"
            false
        } else {
            binding.emailInput.error = null
            true
        }
    }

    fun validatePassword(password: String, binding: FragmentLoginBinding): Boolean {
        return if (password.length < LoginFragment.PASSWORD_MAX_LENGTH) {
            binding.emailInput.isErrorEnabled = true
            binding.passwordInput.error = "Password needs to contain at least 5 characters"
            false
        } else {
            binding.passwordInput.error = null
            true
        }
    }

}