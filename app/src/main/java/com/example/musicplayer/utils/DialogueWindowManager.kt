package com.example.musicplayer.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.musicplayer.R
import kotlin.system.exitProcess

object DialogueWindowManager {
    fun showExitDialogue(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(context.getString(R.string.quit_alert_title))
        builder.setPositiveButton(context.getString(R.string.alert_yes_option)) { _, _ ->
            exitProcess(1)
        }
        builder.setNegativeButton(context.getString(R.string.alert_no_option)) { _, _ ->
        }
        builder.create()
        builder.show()
    }
}