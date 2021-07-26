package com.choondrise.shows_hrvoje_brajko.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.choondrise.shows_hrvoje_brajko.databinding.FragmentRegistrationBinding
import com.choondrise.shows_hrvoje_brajko.models.ListShowsResponse
import com.choondrise.shows_hrvoje_brajko.models.RegisterRequest
import com.choondrise.shows_hrvoje_brajko.models.RegisterResponse
import com.choondrise.shows_hrvoje_brajko.networking.ApiModule
import com.choondrise.shows_hrvoje_brajko.ui.LoginFragment
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegistrationViewModel : ViewModel() {
    private val registrationResultLiveData: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>() }

    fun getRegistrationResultLiveData(): LiveData<Boolean> {
        return registrationResultLiveData
    }

    fun register(email: String, password: String) {
        ApiModule.retrofit.register(RegisterRequest(email, password, password)).enqueue(object :
            Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                registrationResultLiveData.value = response.isSuccessful
            }

            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                registrationResultLiveData.value = false
            }

        })
    }

    fun validateEmail(email: String, binding: FragmentRegistrationBinding) : Boolean {
        val regex = "^[A-Za-z](.*)([@])(.+)(\\.)(.+)".toRegex()
        return if (email.isEmpty()) {
            binding.emailInput.isErrorEnabled = true
            binding.emailInput.error = "Email needs to contain at least 1 character"
            false
        } else if (!email.matches(regex)){
            binding.emailInput.isErrorEnabled = true
            binding.emailInput.error = "Email does not match email regex"
            false
        } else {
            binding.emailInput.error = null
            true
        }
    }

    fun validatePassword(password: String, binding: FragmentRegistrationBinding) : Boolean {
        return if (password.length < LoginFragment.PASSWORD_MAX_LENGTH) {
            binding.emailInput.isErrorEnabled = true
            binding.passwordInput.error = "Password needs to contain at least 5 characters"
            false
        } else {
            binding.passwordInput.error = null
            true
        }
    }

    fun passwordsMatch(binding: FragmentRegistrationBinding) : Boolean {
        return if (binding.editTextPassword.text.toString() != binding.editTextPasswordRepeat.text.toString()) {
            binding.passwordRepeatInput.isErrorEnabled = true
            binding.passwordRepeatInput.error = "Passwords must match!"
            false
        } else {
            binding.passwordRepeatInput.error = null
            true
        }
    }

}