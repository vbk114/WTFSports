package com.e5ctech.wtfsports.accounts.activities

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import bibou.biboubeauty.com.utils.networking.BibouApiClient
import bibou.biboubeauty.com.utils.networking.DefaultResponse
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.accounts.fragments.OtpDialogFragmnt
import com.e5ctech.wtfsports.accounts.models.Users
import com.e5ctech.wtfsports.dashboard.activities.HomeActivity
import com.e5ctech.wtfsports.utils.base.BaseActivity
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginActivity : BaseActivity(), OtpDialogFragmnt.onVerifyOtpListner {

    lateinit var bnLogin: Button
    lateinit var bnSignup:Button
    var users = Users()
    lateinit var etEmail:EditText
    lateinit var etPassword:EditText
    lateinit var tvForgotPassword:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        bnLogin = findViewById(R.id.bnLogin)
        bnSignup = findViewById(R.id.bnSignup)
        etPassword = findViewById(R.id.etPassword)
        etEmail = findViewById(R.id.etEmail)
        tvForgotPassword = findViewById(R.id.tvForgotPassword)

        bnLogin.setOnClickListener {
            users.email = etEmail.text.toString()
            users.password = etPassword.text.toString()
            if(isvalid()){
                loginUser()
            }
        }

        bnSignup.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.putExtra("isForEdit",false)
            startActivity(intent)
        }

        tvForgotPassword.setOnClickListener {
            val intent = Intent(this, ForgotPassword::class.java)
            startActivity(intent)
        }
    }

    fun isvalid(): Boolean {
        if (users.email.isNullOrEmpty()) {
            etEmail.setError("Email should not be Empty")
            etEmail.requestFocus()
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(users.email).matches()) {
            etEmail.setError("Please enter valid Email")
            etEmail.requestFocus()
            return false
        } else {
            etEmail.error = null
        }
        if (users.password.isNullOrEmpty()) {
            etPassword.error = "Password should not be Empty"
            etPassword.requestFocus()
            return false
        } else {
            etPassword.error = null
        }
        return true
    }

    fun loginUser() {
        showProgressDialog()
        val call = BibouApiClient
            .instance(this@LoginActivity)
            .usersApi.loginUser(users)

        call.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {

                if (response.isSuccessful && response.body() != null) {
                    val defaultResponse = response.body();
                    Log.e("TAG", "response 33: " + defaultResponse!!.Responce.email)
                    users = defaultResponse.Responce
                    Log.e("Token...", users.tokens.access)
                    dismissProgressDialog()
                    if(users.isVerified == "True"){
                        saveUsersLocally(users)
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }else{
                        dismissProgressDialog()
                        showOtpFragment(users)
                    }
                } else {
                    dismissProgressDialog()
                    showToast("Invalid credentials")
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                dismissProgressDialog()
                showToast("error occured")
            }
        })
    }

    fun verifyOtp(users: Users) {
        showProgressDialog()

        if(users.isVerified == "True"){
            users.id = decodeString(users.uidb64!!)
        }

        val call = BibouApiClient
            .instance(this@LoginActivity)
            .usersApi.otpverify(users)

        call.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {

                if (response.isSuccessful && response.body() != null) {
                    val defaultResponse = response.body();
                    Log.e("TAG", "response 33: " + defaultResponse!!.Responce.email)
                    val users = defaultResponse.Responce
                    saveUsersLocally(users)
                    dismissProgressDialog()
                    val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                    startActivity(intent)
                } else {
                    dismissProgressDialog()
                    showToast("OTP verification failed")
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                dismissProgressDialog()
                showToast("LogIn failed Please check Username/Password")
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
}