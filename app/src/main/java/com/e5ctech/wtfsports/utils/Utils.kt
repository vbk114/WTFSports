package com.e5ctech.wtfsports.utils

import android.app.Activity
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class Utils {

    companion object {

        fun hideKeyboard(activity: Activity) {
            val imm =
                activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.getCurrentFocus()
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
        }

        fun covertTimeToText(dataDate: String?): String? {
            //    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH)

            var mDateStringTitleResult: String = ""
            //  val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            try {
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"))
                var date = sdf.parse(dataDate)
                var dateGeneral = date
                var mDateUtcNowString: String = sdf.format(Date())
                var now = sdf.parse(mDateUtcNowString)

                Log.e("timedatenow",":::" + now + "---" + date)
                var days = getDateDiff(date!!, now, TimeUnit.MILLISECONDS)
//            return if (days < 7)
//                days.toString() + "d"
//            else
//                (days / 7).toString() + "w"

                var mSeconedValues = days / 1000
                Log.e("daysdays",":::" + days)

                if (days < 1000) {
                    days = days / 1000
                    mDateStringTitleResult = "Just now"
                } else if (days < 1000 * 60) {
                    mDateStringTitleResult = "Just now"
                } else if (days >= 1000 * 60 && days < 1000 * 60 * 60) {
                    //    days = getDateDiff(date!!, now, TimeUnit.MINUTES)

                    var mInt = 1000 * 60
                    var mLongDays = days / mInt
                    days = mLongDays

                    if (days > (1).toLong()) {
                        mDateStringTitleResult = days.toString() + " m"
                    } else {
                        mDateStringTitleResult = days.toString() + " m"
                    }
                } else if (days > 1000 * 60 * 60 && days < 1000 * 60 * 60 * 24) {
                    // Start unit with minutes
                    //     days = getDateDiff(date!!, now, TimeUnit.HOURS)
                    var mInt = 1000 * 60 * 60
                    var mLongDays = days / mInt
                    days = mLongDays
                    //  if (days < 60) {
                    //    days = days / 60
                    if (days > 1) {
                        mDateStringTitleResult = days.toString() + " h"
                    } else {
                        mDateStringTitleResult = days.toString() + " h"
                    }
                } else if (days >= 1000 * 60 * 60 * 24 && days <= 1000 * 60 * 60 * 24 * 7) {
                    //      days = getDateDiff(date!!, now, TimeUnit.DAYS)

                    var mInt = 1000 * 60 * 60 * 24
                    var mLongDays = days / mInt
                    days = mLongDays

                    if (days > 1 && days < 7) {
                        mDateStringTitleResult = days.toString() + " d"
                    } else {
                        mDateStringTitleResult = days.toString() + " d"
                    }

                } else if (days >= 1000 * 60 * 60 * 24 * 7) {
                    var mInt = 1000 * 60 * 60 * 24 * 7
                    var mLongDays = days / mInt
                    days = mLongDays

                    var mCountWeeks = days
                    if (mCountWeeks <= (4).toLong()) {
                        mDateStringTitleResult = mCountWeeks.toString() + " w"
                    } else if (mCountWeeks > (4).toLong()) {
                        var mSmplDateFormate: SimpleDateFormat =
                            SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
                        //  var mDateStringValue: String = mDateAt.split("T").get(0)
                        var dateNative = mSmplDateFormate.format(dateGeneral!!)
                        mDateStringTitleResult = dateNative
                    } else {
                        mDateStringTitleResult = mCountWeeks.toString() + " w"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return mDateStringTitleResult
        }

        fun covertTimeToText1(dataDate: String?): String? {
            //    val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
            val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)

            var mDateStringTitleResult: String = ""
            //  val sdf = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
            try {
                sdf.setTimeZone(TimeZone.getTimeZone("UTC"))
                var date = sdf.parse(dataDate)
                var dateGeneral = date
                var mDateUtcNowString: String = sdf.format(Date())
                var now = sdf.parse(mDateUtcNowString)

                Log.e("timedatenow",":::" + now + "---" + date)
                var days = getDateDiff(date!!, now, TimeUnit.MILLISECONDS)
//            return if (days < 7)
//                days.toString() + "d"
//            else
//                (days / 7).toString() + "w"

                var mSeconedValues = days / 1000
                Log.e("daysdays",":::" + days)

                if (days < 1000) {
                    days = days / 1000
                    mDateStringTitleResult = "Just now"
                } else if (days < 1000 * 60) {
                    mDateStringTitleResult = "Just now"
                } else if (days >= 1000 * 60 && days < 1000 * 60 * 60) {
                    //    days = getDateDiff(date!!, now, TimeUnit.MINUTES)

                    var mInt = 1000 * 60
                    var mLongDays = days / mInt
                    days = mLongDays

                    if (days > (1).toLong()) {
                        mDateStringTitleResult = days.toString() + " m"
                    } else {
                        mDateStringTitleResult = days.toString() + " m"
                    }
                } else if (days > 1000 * 60 * 60 && days < 1000 * 60 * 60 * 24) {
                    // Start unit with minutes
                    //     days = getDateDiff(date!!, now, TimeUnit.HOURS)
                    var mInt = 1000 * 60 * 60
                    var mLongDays = days / mInt
                    days = mLongDays
                    //  if (days < 60) {
                    //    days = days / 60
                    if (days > 1) {
                        mDateStringTitleResult = days.toString() + " h"
                    } else {
                        mDateStringTitleResult = days.toString() + " h"
                    }
                } else if (days >= 1000 * 60 * 60 * 24 && days <= 1000 * 60 * 60 * 24 * 7) {
                    //      days = getDateDiff(date!!, now, TimeUnit.DAYS)

                    var mInt = 1000 * 60 * 60 * 24
                    var mLongDays = days / mInt
                    days = mLongDays

                    if (days > 1 && days < 7) {
                        mDateStringTitleResult = days.toString() + " d"
                    } else {
                        mDateStringTitleResult = days.toString() + " d"
                    }

                } else if (days >= 1000 * 60 * 60 * 24 * 7) {
                    var mInt = 1000 * 60 * 60 * 24 * 7
                    var mLongDays = days / mInt
                    days = mLongDays

                    var mCountWeeks = days
                    if (mCountWeeks <= (4).toLong()) {
                        mDateStringTitleResult = mCountWeeks.toString() + " w"
                    } else if (mCountWeeks > (4).toLong()) {
                        var mSmplDateFormate: SimpleDateFormat =
                            SimpleDateFormat("dd MMM, yyyy", Locale.ENGLISH)
                        //  var mDateStringValue: String = mDateAt.split("T").get(0)
                        var dateNative = mSmplDateFormate.format(dateGeneral!!)
                        mDateStringTitleResult = dateNative
                    } else {
                        mDateStringTitleResult = mCountWeeks.toString() + " w"
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return mDateStringTitleResult
        }

        private fun getDateDiff(date1: Date, date2: Date, timeUnit: TimeUnit): Long {
            val diffInMillies = date2.getTime() - date1.getTime()
            return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS)
        }
    }
}