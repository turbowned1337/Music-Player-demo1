package com.example.musicplayer.data.preferences

import android.content.Context
import com.example.musicplayer.domain.interfaces.ILoginStateRepository

class LoginStateRepository(context: Context) : ILoginStateRepository {
    private val sharedPref = context.getSharedPreferences("LOGIN_DATA", Context.MODE_PRIVATE)

    override fun saveLoginState(newState: Boolean) {
        val editor = sharedPref.edit()

        editor.putBoolean("isLogged", newState)
        editor.apply()
    }

    override fun getLoginState() = sharedPref.getBoolean("isLogged", false)
}