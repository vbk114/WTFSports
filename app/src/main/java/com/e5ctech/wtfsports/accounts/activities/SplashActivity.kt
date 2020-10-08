package com.e5ctech.wtfsports.accounts.activities

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.dashboard.activities.HomeActivity
import com.e5ctech.wtfsports.utils.base.BaseActivity


class SplashActivity : BaseActivity() {

    private var mDelayHandler: Handler? = null
    private val SPLASH_DELAY: Long = 3000 //3 seconds
    private val REQUEST_LOCATION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        gotoNeededActivty()


    }

    private fun gotoNeededActivty() {
        if (getUsersLocally().fullname != null && getUsersLocally().contactno != null && getUsersLocally().contactno!!.isNotEmpty()) {
            Handler().postDelayed({ // This method will be executed once the timer is over
                // Start your app main activity
                val i = Intent(this@SplashActivity, HomeActivity::class.java)
                startActivity(i)

                // close this activity
                finish()
            }, SPLASH_DELAY)
        } else {
            Handler().postDelayed({ // This method will be executed once the timer is over
                // Start your app main activity
                val i = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(i)

                // close this activity
                finish()
            }, SPLASH_DELAY)
        }

    }

}