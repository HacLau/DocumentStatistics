package com.tqs.filecommander

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import com.tqs.filecommander.ui.activity.ScannerActivity
import com.tqs.filecommander.ui.activity.SplashActivity
import com.tqs.filecommander.utils.application
import com.tqs.filecommander.utils.logE

var runningActivities = 0
var inBackgroundTime = 0L

class FileCommanderLifecycle : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

    }

    override fun onActivityStarted(activity: Activity) {
        if (runningActivities == 0) {
            // application from background to foreground
        }

        if (runningActivities == 0 && inBackgroundTime != 0L && System.currentTimeMillis() - inBackgroundTime > 3000) {
            "FileCommanderLifecycle ${activity is ScannerActivity} ".logE()
            if ( activity is ScannerActivity) {

            } else {
                startSplashActivity()
            }
        }

        runningActivities++
    }

    private fun startSplashActivity() {
        application.startActivity(Intent(application, SplashActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        })
    }

    override fun onActivityResumed(activity: Activity) {

    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {
        runningActivities--
        if (runningActivities == 0) {
            // application from fore to background
        }
        inBackgroundTime = System.currentTimeMillis()
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {

    }

}