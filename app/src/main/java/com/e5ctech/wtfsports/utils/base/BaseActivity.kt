package com.e5ctech.wtfsports.utils.base

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.*
import android.graphics.Bitmap
import android.graphics.Color
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.util.Base64
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.accounts.activities.LoginActivity
import com.e5ctech.wtfsports.accounts.models.Users
import com.e5ctech.wtfsports.utils.showcaseutils.CShowProgress
import com.google.android.material.snackbar.Snackbar
import io.github.inflationx.viewpump.ViewPumpContextWrapper
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.nio.charset.StandardCharsets
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


abstract class BaseActivity : AppCompatActivity() {


    val BIBOU_SHARED_PREF = "com.e5ctech.wtfsports.sharedpref"
    var sharedpreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    val USER_ID = "user_id"
    val USER_FIRST_LOGIN = "user_login_first"
    val APK_UPDATE_DISMISSED = "user_update_dismissed"
    val APK_VERSION_CODE = "apk_version_code"
    val APK_VERSION_NAME = "apk_version_name"
    val USER_FIRST_LOGIN_TUTORIAL = "user_login_first_tutorial"
    val USER_TOKEN = "token"

    lateinit var cShowProgress: CShowProgress
    var isOnline = false
    var pendingIntent: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       // setFcmToken();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = resources.getColor(R.color.colorPrimaryDark)
        }

        sharedpreferences = getSharedPreferences(BIBOU_SHARED_PREF, Context.MODE_PRIVATE)
        editor = sharedpreferences!!.edit()

        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR
        broadcastIntent()
        //initializeSocialMediaClients()
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase!!))
    }

   /* fun setFcmToken()
    {
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    return@OnCompleteListener
                }
                // Get new Instance ID token
                val token = task.result?.token
                if (token != null && token.isNotEmpty()) {
                    editor!!.putString(USER_TOKEN, token);
                    Log.e("FCM Token********", token)
                }
            })

        FirebaseMessaging.getInstance().isAutoInitEnabled = true
    }*/

    fun saveUsersLocally(users: Users){
        /*var id: Int? = 0,
    var fullname: String? = null,
    var gender: String? = null,
    var email: String? = null,
    var dob:String?=null,
    var location:String?=null,
    var contactno: String? = null,
    var password: String? = null,*/

        editor!!.putString("id", users.id)
        editor!!.putString("fullname", users.fullname)
        editor!!.putString("gender", users.gender)
        editor!!.putString("email", users.email)
        editor!!.putString("dob", users.dob)
        editor!!.putString("location", users.location)
        editor!!.putString("contactno", users.contactno)
        editor!!.putString("token", users.tokens.access)
        editor!!.apply()
    }

    fun updateUsersLocally(users: Users){
        /*var id: Int? = 0,
    var fullname: String? = null,
    var gender: String? = null,
    var email: String? = null,
    var dob:String?=null,
    var location:String?=null,
    var contactno: String? = null,
    var password: String? = null,*/

        editor!!.putString("fullname", users.fullname)
        editor!!.putString("dob", users.dob)
        editor!!.putString("location", users.location)
        editor!!.putString("contactno", users.contactno)
        editor!!.apply()
    }

    fun setToken(token: String){
        editor!!.putString("token", token)
        editor!!.apply()
    }

    fun getUsersLocally():Users{

        val users = Users()
        users.id = sharedpreferences!!.getString("id", "")!!;
        users.fullname = sharedpreferences!!.getString("fullname", "")!!;
        users.gender = sharedpreferences!!.getString("gender", "")!!;
        users.email = sharedpreferences!!.getString("email", "")!!;
        users.dob = sharedpreferences!!.getString("dob", "")!!;
        users.location = sharedpreferences!!.getString("location", "")!!;
        users.contactno = sharedpreferences!!.getString("contactno", "")!!;
        users.tokens.access = sharedpreferences!!.getString("token", "")!!;
        return users
    }

    fun logout(){

        editor!!.clear()
        editor!!.apply()
        finish()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    fun setApkVersion(versionCode: String, versionname: String) {

        editor!!.putString(APK_VERSION_CODE, versionCode)
        editor!!.putString(APK_VERSION_NAME, versionname)
        editor!!.apply()
    }

    @SuppressLint("HardwareIds")
    fun getDeviceId():String{
        val deviceId = Settings.Secure.getString(
            getContentResolver(),
            Settings.Secure.ANDROID_ID
        );
        return deviceId
    }

    fun getEncodedBase64(value: String): String {
        return encodeString(value)
    }

    fun getEncodedserId(userId: Int): String {
        return getEncodedBase64(userId.toString())
    }

    fun encodeString(s: String):String {
        var data = ByteArray(0)
        try
        {
            data = s.toByteArray(StandardCharsets.UTF_8)
        }
        catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        finally
        {
            val base64Encoded = Base64.encodeToString(data, Base64.DEFAULT)
            return base64Encoded
        }
    }

    fun decodeString(base64:String): String {
        val data: ByteArray = Base64.decode(base64, Base64.DEFAULT)
        val text = String(data, StandardCharsets.UTF_8)
        return text;
    }


    /* fun initializeSocialMediaClients() {

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.request_client_id))
            .requestScopes(RestrictTo.Scope(Scopes.PROFILE))
            .requestEmail().requestProfile()
            .build()

        googleApiClient = GoogleApiClient.Builder(this)
            .enableAutoManage(this) { }
            .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
            .build()

        callbackManager = CallbackManager.Factory.create()
    }

    fun googleSignout() {
        if (googleApiClient != null && googleApiClient!!.isConnected) {
            googleApiClient!!.clearDefaultAccountAndReconnect();
            Auth.GoogleSignInApi.signOut(googleApiClient);
        }
    }

    fun getLoginStatus(): Boolean {
        return sharedpreferences!!.getInt(USER_ID, 0) > 0
    }

    fun gotoAccountActivity() {
        val intent = Intent(this, AccountsActivity::class.java)
        startActivityForResult(intent, CONSTANT_REQUEST_UPDATE_USERS)
    }
*/
    fun showLoadMoreProgress(progressBarBase: ProgressBar) {
        progressBarBase.setVisibility(View.VISIBLE);
    }

    fun hideLoadMoreProgress(progressBarBase: ProgressBar) {
        progressBarBase.setVisibility(View.INVISIBLE)
    }

    override fun setTitle(resId: Int) {
        if (supportActionBar != null) {
            supportActionBar!!.setTitle(resId)
            supportActionBar!!.setDisplayShowTitleEnabled(true)
        }
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(broadcastReceivergg)
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        broadcastIntent()
    }

    fun callPlayStore() {
        val uri = Uri.parse("market://details?id=$packageName")
        val goToMarket = Intent(Intent.ACTION_VIEW, uri)
        goToMarket.addFlags(
            Intent.FLAG_ACTIVITY_NO_HISTORY or
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK
        )
        try {
            startActivity(goToMarket)
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse(getString(R.string.lbl_share_link))
                )
            )
        }
    }

    fun showProgressDialog() {
         cShowProgress = CShowProgress()
         cShowProgress.getInstance(this)
         cShowProgress.showProgress()
    }

    fun dismissProgressDialog() {
        cShowProgress.hideProgress()
    }

    fun showSnackBar(view: View) {
        //Snackbar(view)
        val snackbar = Snackbar.make(
            view, "Internet Not Available! Please turn on Internet",
            Snackbar.LENGTH_LONG
        ).setAction("Action", null)
        snackbar.setActionTextColor(Color.WHITE)
        val snackbarView = snackbar.view
        snackbarView.setBackgroundColor(Color.DKGRAY)
        val textView =
            snackbarView.findViewById(R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        textView.textSize = 14f
        snackbar.show()
    }

    private var broadcastReceivergg: BroadcastReceiver = object : BroadcastReceiver() {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == ConnectivityManager.CONNECTIVITY_ACTION) {
                val networkInfo =
                    intent.getParcelableExtra<NetworkInfo>(ConnectivityManager.EXTRA_NETWORK_INFO)
                if (networkInfo != null && networkInfo.detailedState == NetworkInfo.DetailedState.CONNECTED) {
                    Log.d("Network", "Internet YAY")
                    isOnline = true
                } else if (networkInfo != null && networkInfo.detailedState == NetworkInfo.DetailedState.DISCONNECTED) {
                    Log.d("Network", "No internet :(")
                    isOnline = false
                    showToast("Internet not available. please connect your internet")
                }
            }
        }
    }

    fun broadcastIntent() {
        registerReceiver(broadcastReceivergg, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun setBackEnabled(flag: Boolean) {
        if (supportActionBar != null) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(flag)
            supportActionBar!!.setHomeAsUpIndicator(R.drawable.arrow)
            supportActionBar!!.setHomeButtonEnabled(flag)
        }
    }

    fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }

    fun onShareClick(subject: String, extraText: String) {
        val targetShareIntents = ArrayList<Intent>()
        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND
        shareIntent.setType("text/plain");
        val resInfos = packageManager.queryIntentActivities(shareIntent, 0)
        if (!resInfos.isEmpty()) {
            println("Have package")
            for (resInfo in resInfos) {
                val packageName = resInfo.activityInfo.packageName
                Log.i("Package Name", packageName)
                if (packageName.contains("twitter")
                    || packageName.contains("facebook")
                    || packageName.contains("whatsapp")
                    || packageName.contains("skype")
                    || packageName.contains("mms")
                    || packageName.contains("android.email")
                    || packageName.contains("android.gm")
                    || packageName.contains("instagram")
                    || packageName.contains("shareme")
                    || packageName.contains("telegram")
                ) {
                    val intent = Intent()
                    intent.component = ComponentName(packageName, resInfo.activityInfo.name)
                    intent.action = Intent.ACTION_SEND
                    intent.type = "text/plain"
                    intent.putExtra(Intent.EXTRA_TEXT, extraText)
                    intent.putExtra(Intent.EXTRA_SUBJECT, subject)
                    intent.setPackage(packageName)
                    targetShareIntents.add(intent)
                }
            }
            if (!targetShareIntents.isEmpty()) {
                println("Have Intent")
                val chooserIntent =
                    Intent.createChooser(targetShareIntents.removeAt(0), "Choose app to share")
                chooserIntent.putExtra(
                    Intent.EXTRA_INITIAL_INTENTS,
                    targetShareIntents.toArray(arrayOf<Parcelable>())
                )
                startActivity(chooserIntent)
            } else {
                println("Do not Have Intent")
            }
        }
    }

    fun saveImageAndShare(bitmap: Bitmap) {

        try {
            var cachePath = File(cacheDir, "images")
            if (!cachePath.exists()) {
                cachePath.mkdirs() // don't forget to make the directory
            }
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            cachePath = File(
                cachePath.path + File.separator
                        + "IMG_" + timeStamp + ".png"
            )
            val stream = FileOutputStream(cachePath, false) // overwrites this image every time
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun shareImageUri() {
        val imagePath = File(cacheDir, "images")
        val files = imagePath.listFiles()
        val uriList = ArrayList<Uri>()

        for (file in files) {
            val contentUri =
                FileProvider.getUriForFile(this, "com.e5ctech.wtfsports.fileprovider", file)
            if (contentUri != null) {
                uriList.add(contentUri)
            }
        }

        val shareIntent = Intent()
        shareIntent.action = Intent.ACTION_SEND_MULTIPLE
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
        shareIntent.type = "image/*"
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
        startActivity(Intent.createChooser(shareIntent, "Choose app to share"))
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

}