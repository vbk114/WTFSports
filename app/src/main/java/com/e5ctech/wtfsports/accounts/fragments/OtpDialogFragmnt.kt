package com.e5ctech.wtfsports.accounts.fragments


import android.app.Dialog
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.DisplayMetrics
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.accounts.models.Users
import com.e5ctech.wtfsports.utils.base.BaseDialogFragment


class OtpDialogFragmnt : BaseDialogFragment() {


    override fun setUp() {
    }

    lateinit var rootView: View;
    private lateinit var toolbar: Toolbar

    lateinit var bnApply: TextView

    lateinit var et1value: String
    lateinit var et2value: String
    lateinit var et3value: String
    lateinit var et4value: String
    lateinit var otpValue: String
    lateinit var et5value: String
    lateinit var et6value: String

    lateinit var et1: EditText
    lateinit var et2: EditText
    lateinit var et3: EditText
    lateinit var et4: EditText
    lateinit var et5:EditText
    lateinit var et6:EditText

    lateinit var users: Users

    private var callback: onVerifyOtpListner? = null

    interface onVerifyOtpListner {
        fun onVerifyOtp(isVerified: Boolean, users: Users)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        rootView = inflater.inflate(R.layout.fragment_otp_dialog_fragmnt, container, false)
        getExtras()
        initViews()
        return rootView;
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if(targetFragment!=null){
            callback = targetFragment as onVerifyOtpListner
        }else{
            callback = context as onVerifyOtpListner
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        dialog!!.setCancelable(false)
        getOtpListerners()

        bnApply.setOnClickListener {
            getOtpValues()
            if (otpValue != null && otpValue.isNotEmpty() && users.otp!!.trim() == otpValue.trim()){
                callback!!.onVerifyOtp(true, users)
                dismiss()
            }else{
                Toast.makeText(context, "Otp does not matches", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
    fun getOtpValues() {

        et1value = et1.text.toString()
        et2value = et2.text.toString()
        et3value = et3.text.toString()
        et4value = et4.text.toString()
        et5value = et5.text.toString()
        et6value = et6.text.toString()

        otpValue = et1value + et2value + et3value + et4value + et5value + et6value
        users.id = getBaseActivity()!!.getUsersLocally().id
    }

    fun getExtras(){
        if(arguments!=null && !requireArguments().isEmpty){
            users = requireArguments().getSerializable("users") as Users
        }
    }

    fun initViews() {

        bnApply = rootView.findViewById(R.id.bnApply)
        toolbar = rootView.findViewById(R.id.toolbar)
        et1 = rootView.findViewById(R.id.et1)
        et2 = rootView.findViewById(R.id.et2)
        et3 = rootView.findViewById(R.id.et3)
        et4 = rootView.findViewById(R.id.et4)
        et5 = rootView.findViewById(R.id.et5)
        et6 = rootView.findViewById(R.id.et6)

        toolbar.setNavigationIcon(R.drawable.arrow)

        toolbar.title = "Verify OTP"
        isCancelable = false
        toolbar.setNavigationOnClickListener {
            //callback!!.onVerifyOtp(false,users);
            dismiss()
        }
    }

    fun getOtpListerners() {

        et1.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    et2.requestFocus()
                } else if (s.isEmpty()) {
                    et1.clearFocus()
                }
            }
        })

        et2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    et3.requestFocus()
                } else if (s.isEmpty()) {
                    et2.clearFocus()
                }
            }
        })

        et3.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {
                if (s.length == 1) {
                    et4.requestFocus()
                } else if (s.isEmpty()) {
                    et3.clearFocus()
                }
            }
        })

        et4.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {

                if (s.length == 1) {
                    et5.requestFocus();
                } else if (s.isEmpty()) {
                    et4.clearFocus();
                }
            }
        })

        et5.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {

                if (s.length == 1) {
                    et6.requestFocus();
                } else if (s.isEmpty()) {
                    et5.clearFocus();
                }
            }
        })

        et6.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable) {

                if (s.length == 1) {
                    et6.clearFocus();
                } else if (s.isEmpty()) {
                    et6.requestFocus();
                }
            }
        })

        et6.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {

                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    et5.requestFocus()
                }
                return false
            }
        })

        et5.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {

                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    et4.requestFocus()
                }
                return false
            }
        })

        et4.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {

                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    et3.requestFocus()
                }
                return false
            }
        })

        et3.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {

                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    et2.requestFocus()
                }
                return false
            }
        })

        et2.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {

                //You can identify which key pressed buy checking keyCode value with KeyEvent.KEYCODE_
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    //this is for backspace
                    et1.requestFocus()
                }
                return false
            }
        })
    }


    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val iWidth = getScreenWidth(dialog.context)
            val iHeight = getScreenHeight(dialog.context)
            val params = dialog.window!!.attributes
            params.height = (iHeight * .40).toInt()
            params.width = (iWidth * .90).toInt()
            dialog.window!!.attributes = params
        }
    }

    fun getScreenWidth(context: Context): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val display = wm.defaultDisplay
        val size = Point()
        // display.getSize(size);
        var width = 0
        try {
            display.getRealSize(size)
            width = size.x
        } catch (e: Throwable) {
            width = display.width
        }

        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)

        return width
    }

    fun getScreenHeight(context: Context): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager

        val display = wm.defaultDisplay
        val size = Point()
        // display.getSize(size);
        var height = 0
        try {
            display.getRealSize(size)
            height = size.y
        } catch (e: NoSuchMethodError) {
            height = display.height
        }

        val metrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(metrics)

        return height
    }


}
