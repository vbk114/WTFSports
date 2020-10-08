package com.e5ctech.wtfsports.accounts.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import bibou.biboubeauty.com.utils.networking.BibouApiClient
import bibou.biboubeauty.com.utils.networking.DefaultResponse
import bibou.biboubeauty.com.utils.networking.OtpResponse
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.accounts.models.Users
import com.e5ctech.wtfsports.utils.base.BaseActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : BaseActivity() {

    lateinit var etConfirmPassword:EditText
    lateinit var etPassword:EditText
    lateinit var bnSumbit:Button
    lateinit var otpResponse: OtpResponse
    var users = Users()
    private lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        getExtras()
        toolbar = findViewById(R.id.toolbar)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)
        bnSumbit = findViewById(R.id.bnSumbit)

        setSupportActionBar(toolbar)
        setBackEnabled(true)
        toolbar.title = "Reset Password"

        bnSumbit.setOnClickListener {
           users.password = etConfirmPassword.text.toString()
           users.token = otpResponse.token
            users.uidb64 = otpResponse.uidb64
            if(isValid()){
                resetPassword()
            }
        }

    }

    fun getExtras(){
        if(intent!=null && intent.extras!=null && !intent.extras!!.isEmpty){
            otpResponse = intent.getSerializableExtra("otpResponse") as OtpResponse
        }
    }

    fun isValid():Boolean{
        if(etPassword.text.toString() == null || etPassword.text.isEmpty()){
            showToast("Password should not empty")
            return false
        }else if(etConfirmPassword.text.toString() == null || etConfirmPassword.text.isEmpty()){
            showToast("Confirm Password should not empty")
            return false
        }else if(etPassword.text.toString() != etConfirmPassword.text.toString()){
            showToast("Password does not matches")
            return false
        }
        return true
    }

    fun resetPassword() {
        showProgressDialog()
        val call = BibouApiClient
            .instance(this@ResetPasswordActivity)
            .usersApi.setnewPassword(users)

        call.enqueue(object : Callback<OtpResponse> {
            override fun onResponse(
                call: Call<OtpResponse>,
                response: Response<OtpResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    showToast("Reset password successfully")
                    finish()
                } else {
                    try {
                        dismissProgressDialog()
                    }catch (ex:Exception){

                    }
                }
            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                dismissProgressDialog()
                showToast("error occured")
            }
        })
    }
}