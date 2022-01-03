package com.frankliang.findburritoproject.util

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.Settings
import android.view.View
import android.widget.TextView
import com.frankliang.findburritoproject.R
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception

object SnackBarUtil {
    fun showSnackBarWithSetting(activity: Activity, view: View, resId: Int) {
        try {
            val snackBar = Snackbar.make(view, resId, Snackbar.LENGTH_LONG)
                .setAction(activity.getString(R.string.open_settings)) {
                    val uri = Uri.fromParts("package", activity.applicationContext.packageName, null)
                    val intent = Intent().apply {
                        action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                        data = uri
                    }
                    activity.finish()
                    activity.startActivity(intent)
                }
            val tv = snackBar.view.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            tv.setTextColor(Color.WHITE)
            snackBar.show()
        } catch (e: Exception) {

        }
    }
}