package com.e5ctech.wtfsports.accounts.activities

import android.Manifest
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import bibou.biboubeauty.com.utils.networking.BibouApiClient
import bibou.biboubeauty.com.utils.networking.CountryResponse
import bibou.biboubeauty.com.utils.networking.DefaultResponse
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.accounts.adapters.ArticlesAdapter
import com.e5ctech.wtfsports.accounts.fragments.OtpDialogFragmnt
import com.e5ctech.wtfsports.accounts.models.Users
import com.e5ctech.wtfsports.utils.base.BaseActivity
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RegisterActivity : BaseActivity(),AdapterView.OnItemSelectedListener,OtpDialogFragmnt.onVerifyOtpListner,com.google.android.gms.location.LocationListener {

    lateinit var bnNext:Button;
    private lateinit var toolbar: Toolbar
    var users = Users()
    lateinit var etFullName: EditText
    lateinit var etPhoneNo: EditText
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var spGender: Spinner
    lateinit var spDay: Spinner
    lateinit var spMonth: Spinner
    lateinit var spYear: Spinner
    lateinit var etCountryCode: EditText
    var dayList = (1..31).toList()
    var monthList = (1..12).toList()
    var yearList = (1950..2010).toList()
    var countryCodeList = mutableListOf<String>()
    var countryList = mutableListOf<String>()
    var day = ""
    var month = ""
    var year = ""
    var isForEdit = false;
    lateinit var tvSignUp:TextView
    lateinit var spCountry:Spinner
    lateinit var country:String

    @RequiresApi(Build.VERSION_CODES.N)
    val myCalendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        bnNext = findViewById(R.id.bnNext)
        toolbar = findViewById(R.id.toolbar)
        etFullName = findViewById(R.id.etFullName)
        etPhoneNo = findViewById(R.id.etPhoneNo)
        etPassword = findViewById(R.id.etPassword)
        etEmail = findViewById(R.id.etEmail)
        spGender = findViewById(R.id.spGender)
        spDay = findViewById(R.id.spDay)
        spMonth = findViewById(R.id.spMonth)
        spYear = findViewById(R.id.spYear)
        etCountryCode = findViewById(R.id.etCountryCode)
        tvSignUp = findViewById(R.id.tvSignUp)
        spCountry = findViewById(R.id.spCountry)


        setSupportActionBar(toolbar)
        toolbar.title = "Create Account"
        setBackEnabled(true)

        getExtras()

        val genderList = ArrayList<String>()
        genderList.add("Select Gender")
        genderList.add("MALE")
        genderList.add("FEMALE")
        genderList.add("OTHER")

        val arrayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            genderList
        )
        spGender.adapter = arrayAdapter;


        val dayAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            dayList
        )
        spDay.adapter = dayAdapter

        val monthAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            monthList
        )
        spMonth.adapter = monthAdapter

        val yearAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            yearList
        )
        spYear.adapter = yearAdapter

        if(isForEdit){
            toolbar.setTitle("Edit Profile")
            tvSignUp.visibility = View.GONE
            etPassword.visibility = View.GONE
            etEmail.isEnabled = false
            etCountryCode.isEnabled = false
            bnNext.text = "Update"

            etCountryCode.isClickable = false
            setuserdetails()
        }
        getCountryList()
        bnNext.setOnClickListener {
            users.fullname = etFullName.text.toString()
            users.email = etEmail.text.toString()
            users.dob = "$year-$month-$day"
            users.contactno = etPhoneNo.text.toString()
            users.location = "location"

            if(isvalid() && !isForEdit){
                saveUser()
            } else if(isForEdit && isvalidEdit()){
                updateUser()
            }
        }

        tvSignUp.setOnClickListener {
            finish()
        }

        spGender.onItemSelectedListener = this
        spDay.onItemSelectedListener = this
        spMonth.onItemSelectedListener = this
        spYear.onItemSelectedListener = this
        spCountry.onItemSelectedListener = this

    }

    private fun getExtras(){
        if(intent !=null && intent.extras!=null && !intent.extras!!.isEmpty){
            isForEdit = intent.getBooleanExtra("isForEdit",false)
        }
    }

    fun getCountryList() {
        showProgressDialog()
        val call = BibouApiClient
            .instance(this@RegisterActivity)
            .usersApi.getCountryList()

        call.enqueue(object : Callback<CountryResponse> {
            override fun onResponse(
                call: Call<CountryResponse>,
                response: Response<CountryResponse>
            ) {

                if (response.isSuccessful && response.body() != null) {
                    val articleResponse = response.body();
                    getCountries(articleResponse!!)
                    getCountryCodeList(articleResponse)
                    setCountryAdapter()
                    dismissProgressDialog()

                } else {
                    dismissProgressDialog()

                }
            }

            override fun onFailure(call: Call<CountryResponse>, t: Throwable) {
                dismissProgressDialog()
            }
        })
    }

    fun setCountryAdapter(){
        val arrayCountryAdapter = ArrayAdapter<String>(
            this@RegisterActivity,
            android.R.layout.simple_list_item_1,
            countryList
        )
        spCountry.adapter = arrayCountryAdapter;
    }

    fun getCountries(countryResponse: CountryResponse){

        for( countries in countryResponse.Responce){
            countryList.add(countries.nicename)
        }
    }

    fun getCountryCodeList(countryResponse: CountryResponse){

        for( countries in countryResponse.Responce){
            countryCodeList.add(countries.phonecode)
        }
    }

    fun setuserdetails(){

        etEmail.setText(getUsersLocally().email)
        etFullName.setText(getUsersLocally().fullname)
        etPhoneNo.setText(getUsersLocally().contactno)
        if (getUsersLocally().gender.equals("MALE")) {
            spGender.setSelection(1)
        } else if (getUsersLocally().gender.equals("FEMALE")) {
            spGender.setSelection(2)
        }else if (getUsersLocally().gender.equals("OTHER")) {
            spGender.setSelection(3)
        }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

        when(parent!!.id){

            R.id.spGender->{
                users.gender = spGender.getItemAtPosition(position).toString()
            }
            R.id.spDay->{
                day = spDay.getItemAtPosition(position).toString()
            }
            R.id.spMonth->{
                month = spMonth.getItemAtPosition(position).toString()
            }
            R.id.spYear->{
                year = spYear.getItemAtPosition(position).toString()
            }
            R.id.spCountry->{
                country = spCountry.getItemAtPosition(position).toString()
                etCountryCode.setText(countryCodeList.get(position))
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }

    @RequiresApi(Build.VERSION_CODES.N)
    var date =
        OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateLabel()
        }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateLabel() {
        val myFormat = "YYYY-MM-dd" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
       // etDob.setText(sdf.format(myCalendar.time))
    }

    fun isvalid(): Boolean {

        if (users.fullname.isNullOrEmpty()) {
            etFullName.error = "Name should not be Empty"
            etFullName.requestFocus()
            return false
        } else {
            etFullName.error = null
        }

        if( users.gender.isNullOrEmpty()){
            showToast("Please select gender")
            return false
        }else if(users.gender.equals("Select Gender")){
            showToast("Please select gender")
        }

        if(day.isEmpty()){
            showToast("Please select day of birth")
            return false
        }
        if(month.isEmpty()){
            showToast("Please select month of birth")
            return false
        }
        if(year.isEmpty()){
            showToast("Please year of birth")
            return false
        }

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

        users.password = etPassword.text.toString()

        if (users.password.isNullOrEmpty()) {
            etPassword.error = "Password should not be Empty"
            etPassword.requestFocus()
            return false
        } else {
            etPassword.error = null
        }

        if (users.contactno.isNullOrEmpty()) {
            etPhoneNo.error = "Contact no should not be Empty"
            etPhoneNo.requestFocus()
            return false
        } else if (!Patterns.PHONE.matcher(users.contactno).matches()) {
            etPhoneNo.error = "Please enter valid contact no"
            etPhoneNo.requestFocus()
            return false
        } else {
            etPhoneNo.error = null
        }

        return true
    }

    fun isvalidEdit(): Boolean {

        if (users.fullname.isNullOrEmpty()) {
            etFullName.error = "Name should not be Empty"
            etFullName.requestFocus()
            return false
        }

        if( users.gender.isNullOrEmpty()){
            showToast("Please select gender")
            return false
        }else if(users.gender.equals("Select Gender")){
            showToast("Please select gender")
            return false
        }

        if(day.isEmpty()){
            showToast("Please select day of birth")
            return false
        }
        if(month.isEmpty()){
            showToast("Please select month of birth")
            return false
        }
        if(year.isEmpty()){
            showToast("Please year of birth")
            return false
        }

        if (users.email.isNullOrEmpty()) {
            etEmail.error = "Email should not be Empty"
            etEmail.requestFocus()
            return false
        } else if (!Patterns.EMAIL_ADDRESS.matcher(users.email).matches()) {
            etEmail.error = "Please enter valid Email"
            etEmail.requestFocus()
            return false
        }

        if (users.contactno.isNullOrEmpty()) {
            etPhoneNo.error = "Contact no should not be Empty"
            etPhoneNo.requestFocus()
            return false
        } else if (!Patterns.PHONE.matcher(users.contactno).matches()) {
            etPhoneNo.error = "Please enter valid contact no"
            etPhoneNo.requestFocus()
            return false
        }
        return true
    }

    fun saveUser() {
        showProgressDialog()
        val call = BibouApiClient
            .instance(this@RegisterActivity)
            .usersApi.saveUser(users, "f363466b9dbc0091276c51d484addc2acd980956")

        call.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {

                if (response.isSuccessful) {

                    val defaultResponse = response.body();
                    Log.e("TAG", "response 33: " + defaultResponse!!.Responce.email)
                    val users = defaultResponse.Responce
                    saveUsersLocally(users);
                    dismissProgressDialog()
                    showOtpFragment(users)
                } else {
                    try {
                        dismissProgressDialog()
                        showToast("Email or Mobile no already exists ! Please Register new one")
                        val jObjError = JSONObject(response.errorBody()!!.string())
                        if(jObjError.getString("detail").isNotEmpty()){
                            showToast(jObjError.getString("detail"))
                        }
                    }catch (ex:Exception){
                    }
                }

            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                dismissProgressDialog()
                Toast.makeText(
                    this@RegisterActivity,
                    "LogIn failed Please check Username/Password",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
        })
    }

    fun updateUser() {
        showProgressDialog()
        val call = BibouApiClient
            .instance(this@RegisterActivity)
            .usersApi.updateUser(getUsersLocally().id!!,users)

        call.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {

                if (response.isSuccessful) {

                    val defaultResponse = response.body();
                    Log.e("TAG", "response 33: " + defaultResponse!!.Responce.email)
                    val users = defaultResponse.Responce
                    saveUsersLocally(users);
                    dismissProgressDialog()
                    showOtpFragment(users)
                } else {
                    try {
                        dismissProgressDialog()
                        showToast("Error occured")
                    }catch (ex:Exception){
                    }
                }
            }

            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                dismissProgressDialog()
              showToast("Error occured")
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
            .instance(this@RegisterActivity)
            .usersApi.otpverify(users)

        call.enqueue(object : Callback<DefaultResponse> {
            override fun onResponse(
                call: Call<DefaultResponse>,
                response: Response<DefaultResponse>
            ) {

                if (response.isSuccessful && response.body() != null) {
                    val defaultResponse = response.body();
                    Log.e("TAG", "response 33: " + defaultResponse!!.Responce.email)
                    val users = defaultResponse.Responce
                    saveUsersLocally(users);
                    dismissProgressDialog()
                    val intent = Intent(this@RegisterActivity, TeamSelectionActivity::class.java)
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

    override fun onLocationChanged(location: Location?) {
        showToast(location!!.longitude.toString()+ location.latitude.toString())
    }
}