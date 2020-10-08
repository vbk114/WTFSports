package com.e5ctech.wtfsports.utils.showcaseutils

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.e5ctech.wtfsports.R


class CShowProgress {

    var mCShowProgress: CShowProgress? = null
    var mDialog: Dialog? = null

    fun getInstance(mContext: Context): CShowProgress {
        if (mCShowProgress == null) {
            mCShowProgress = CShowProgress()
            mDialog = Dialog(mContext)
        }
        return mCShowProgress as CShowProgress
    }

    fun showProgress() {

        mDialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        mDialog!!.setContentView(R.layout.custom_progress_layout)
        mDialog!!.findViewById<View>(R.id.progress_bar).visibility = View.VISIBLE
        mDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        mDialog!!.window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        mDialog!!.setCancelable(false)
        mDialog!!.setCanceledOnTouchOutside(false)
        mDialog!!.show()
    }

    fun hideProgress() {
        if (mDialog != null) {
            mDialog!!.dismiss()
            mDialog = null
        }
    }
}
