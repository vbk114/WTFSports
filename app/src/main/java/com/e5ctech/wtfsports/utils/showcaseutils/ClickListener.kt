package com.e5ctech.wtfsports.utils.showcaseutils

import android.view.View

interface ClickListener {
    fun onClick(view: View?, position: Int)
    fun onLongClick(view: View?, position: Int)
}