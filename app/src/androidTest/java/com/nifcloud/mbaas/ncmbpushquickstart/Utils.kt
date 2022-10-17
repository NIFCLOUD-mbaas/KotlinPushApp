package com.nifcloud.mbaas.ncmbpushquickstart

import android.os.Build
import android.util.Log
import androidx.test.platform.app.InstrumentationRegistry.getInstrumentation
import androidx.test.uiautomator.UiDevice
import androidx.test.uiautomator.UiObjectNotFoundException
import androidx.test.uiautomator.UiSelector
import com.nifcloud.mbaas.core.*


private const val TAG = "FcmService"
const val NOTIFICATION_TITLE = "UITest push notification"
const val NOTIFICATION_TEXT = "Thank you! We appreciate your business, and we’ll do our best to continue to give you the kind of service you deserve."

object Utils {
    fun sendPushWithSearchCondition() {
        val installation = NCMBInstallation.currentInstallation
        installation.getDeviceTokenInBackground(NCMBCallback { e, token ->
            if (e != null) {
                Log.d("error","保存に失敗しました : " + e.message)
            } else {
                //保存に成功した場合の処理
                val result = token as String
                val query = NCMBQuery.forInstallation()
                query.whereEqualTo("deviceToken", token)
                val push = NCMBPush()
                push.isSendToAndroid = true
                push.title = NOTIFICATION_TITLE
                push.message = NOTIFICATION_TEXT
                push.immediateDeliveryFlag =  true
                try {
                    push.save()
                }
                catch (error:NCMBException){
                    throw error
                }
            }
        })
    }

    internal fun allowPermissionsIfNeeded() {
        if (Build.VERSION.SDK_INT >= 33) {
            val device = UiDevice.getInstance(getInstrumentation())
            val allowPermissions = device.findObject(UiSelector().text("Allow"))
            if (allowPermissions.exists()) {
                try {
                    allowPermissions.click()
                } catch (e: UiObjectNotFoundException) {
                    Log.d(TAG, "Error: " + e.message)
                }
            }
        }
    }
}