package com.e5ctech.wtfsports.accounts.activities

import android.content.Intent
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
import com.e5ctech.wtfsports.accounts.fragments.OtpDialogFragmnt
import com.e5ctech.wtfsports.accounts.models.Users
import com.e5ctech.wtfsports.dashboard.activities.HomeActivity
import com.e5ctech.wtfsports.utils.base.BaseActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotPassword : BaseActivity(),OtpDialogFragmnt.onVerifyOtpListner {

    private lateinit var toolbar: Toolbar
    private lateinit var bnReset:Button
    private lateinit var etEmail:EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        toolbar = findViewById(R.id.toolbar)
        bnReset = findViewById(R.id.bnReset)
        etEmail = findViewById(R.id.etEmail)
        setSupportActionBar(toolbar)
        setBackEnabled(true)
        toolbar.title = "Forgot Password"

        bnReset.setOnClickListener {

            val email = etEmail.text.toString()
            val users = Users()
            users.email = email
            sendresetPassword(users)
        }

    }

    fun sendresetPassword(users: Users) {
        showProgressDialog()
        val call = BibouApiClient
            .instance(this@ForgotPassword)
            .usersApi.resetRequest(users)

        call.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {

                if (response.isSuccessful && response.body() != null) {
                    val defaultResponse = response.body();
                    Log.e("TAG", "response 33: " + defaultResponse!!.Responce.email)
                    val users = defaultResponse.Responce
                    showOtpFragment(users)
                } else {
                    try {
                        dismissProgressDialog()
                    }catch (ex:Exception){

                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                dismissProgressDialog()
                showToast("error occured")
            }
        })
    }

    fun showOtpFragment(users: Users){
        val bundle = Bundle()
        bundle.putSerializable("users", users)
        val otpDialogFragmnt = OtpDialogFragmnt()
        otpDialogFragmnt.arguments = bundle
        otpDialogFragmnt.show(supportFragmentManager, "OTPfragment")
    }

    override fun onVerifyOtp(isVerified: Boolean, users: Users) {
        verifyOtp(users)
    }

    fun verifyOtp(users: Users) {
        showProgressDialog()
        val call = BibouApiClient
            .instance(this@ForgotPassword)
            .usersApi.restOtpverify(users)

        call.enqueue(object : Callback<OtpResponse> {
            override fun onResponse(
                call: Call<OtpResponse>,
                response: Response<OtpResponse>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val defaultResponse = response.body()
                    dismissProgressDialog()
                    val intent = Intent(this@ForgotPassword, ResetPasswordActivity::class.java)
                    intent.putExtra("otpResponse",defaultResponse)
                    startActivity(intent)
                    finish()
                } else {
                    dismissProgressDialog()
                    showToast("OTP verification failed")
                }
            }

            override fun onFailure(call: Call<OtpResponse>, t: Throwable) {
                dismissProgressDialog()
            }
        })
    }

}