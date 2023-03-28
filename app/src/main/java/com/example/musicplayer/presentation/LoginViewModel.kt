package com.example.musicplayer.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicplayer.domain.entities.User
import com.example.musicplayer.domain.usecases.UserUseCase
import com.example.musicplayer.utils.UserDataValidator
import kotlinx.coroutines.launch

enum class LoginResponse {
    WAITING_FOR_DATA,
    SUCCESSFUL,
    NOT_SUCCESSFUL,
}

class LoginViewModel(
    private val userUseCase: UserUseCase,
    val validator: UserDataValidator = UserDataValidator()
) : ViewModel() {

    private val _isLoginSuccessful = MutableLiveData(LoginResponse.WAITING_FOR_DATA)
    val isLoginSuccessful: LiveData<LoginResponse>
        get() = _isLoginSuccessful

    private val _isRegisterButtonEnabled = MutableLiveData(false)
    val isRegisterButtonEnabled: LiveData<Boolean>
        get() = _isRegisterButtonEnabled

    fun setRegisterButtonEnabled(newState: Boolean) = _isRegisterButtonEnabled.postValue(newState)

    fun registerUser(user: User) {
        viewModelScope.launch {
            userUseCase.addUser(user)
        }
        setLoginState(true)
    }

    fun tryLoginUser(login: String, password: String) {
        viewModelScope.launch {
            val dataFromDB = userUseCase.getUserData(login)
            if (validator.checkLoginData(login, password, dataFromDB)) {
                setLoginState(true)
                _isLoginSuccessful.postValue(LoginResponse.SUCCESSFUL)
            } else {
                _isLoginSuccessful.postValue(LoginResponse.NOT_SUCCESSFUL)
            }
        }
    }

    fun setLoginState(newState: Boolean) = userUseCase.saveLoginState(newState)

    fun isLogged() = userUseCase.getLoginState()

    fun resetLoginResponse() = _isLoginSuccessful.postValue(LoginResponse.WAITING_FOR_DATA)
}