package com.example.musicplayer.utils

import android.text.TextUtils
import com.example.musicplayer.domain.entities.User
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class UserDataValidator {
    fun validateFields(inputFields: Array<TextInputEditText>) {
        inputFields[0].validate("Некорретный логин") { s -> validateString(s) }
        inputFields[1].validate("Некорректный E-Mail") { s -> validateEmail(s) }
        inputFields[2].validate("Некорректный пароль") { s -> validateString(s) }
        inputFields[3].validate("Пароли не совпадают") {
            validatePassword(
                inputFields[2].text.toString(),
                inputFields[3].text.toString()
            )
        }
    }

    fun setValidationListeners(inputFields: Array<TextInputEditText>) {
        for (element in inputFields) {
            element.afterTextChanged {
                (element.parent.parent as TextInputLayout).error = null
            }
        }
    }

    fun hasNoErrors(inputFields: Array<TextInputEditText>): Boolean {
        var result = true
        for (element in inputFields) {
            result = result && (element.parent.parent as TextInputLayout).error == null
        }
        return result
    }

    private fun validateString(data: String): Boolean {
        return (!data.matches(".*\\s.*".toRegex()) && data.isNotEmpty())
    }

    private fun validateEmail(data: String): Boolean {
        return (!TextUtils.isEmpty(data)
                && android.util.Patterns.EMAIL_ADDRESS.matcher(data).matches())
    }

    private fun validatePassword(password: String, repeatedPassword: String): Boolean {
        return (password == repeatedPassword && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(
            repeatedPassword
        ))
    }

    fun checkLoginData(login: String, password: String, userDBEntityData: User?): Boolean {
        return if (userDBEntityData != null) (login == userDBEntityData.login && password == userDBEntityData.password)
        else false
    }
}