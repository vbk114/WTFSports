package com.e5ctech.wtfsports.widgets

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.KeyEvent
import androidx.appcompat.widget.AppCompatEditText

class KeyboardEditText : AppCompatEditText {
    constructor(context: Context) : super(context) {}
    constructor(context: Context?, attrs: AttributeSet?) : super(
        context!!,
        attrs
    ) {
    }

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context!!, attrs, defStyleAttr) {

    }

    override fun onFocusChanged(
        focused: Boolean,
        direction: Int,
        previouslyFocusedRect: Rect?
    ) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (listener != null) listener!!.onStateChanged(this, true)
    }

    override fun onKeyPreIme(keyCode: Int, event: KeyEvent): Boolean {
        if (event.keyCode == KeyEvent.KEYCODE_BACK
            && event.action == KeyEvent.ACTION_UP
        ) {
            if (listener != null) listener!!.onStateChanged(this, false)
        }
        return super.onKeyPreIme(keyCode, event)
    }

    /**
     * Keyboard Listener
     */
    var listener: KeyboardListener? = null



    interface KeyboardListener {
        fun onStateChanged(
            keyboardEditText: KeyboardEditText?,
            showing: Boolean
        )
    }
}