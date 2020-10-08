package com.e5ctech.wtfsports.dashboard.activities

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.Toolbar
import com.e5ctech.wtfsports.R
import com.e5ctech.wtfsports.utils.base.BaseActivity

class GenericWebViewActivity : BaseActivity() {

    lateinit var webView:WebView
    lateinit var url:String
    lateinit var toolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generic_web_view)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        setBackEnabled(true)
        val intent = intent
        if(intent!=null && intent.extras!=null && !intent.extras!!.isEmpty){
            url = intent.getStringExtra("url")!!
        }

        webView = findViewById(R.id.webView)
        webView.webViewClient = MyWebViewClient(this)
        webView.loadUrl(url)
    }

    class MyWebViewClient internal constructor(private val activity: BaseActivity) : WebViewClient() {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            activity.showProgressDialog()
            val url: String = request?.url.toString();
            view?.loadUrl(url)
            activity.dismissProgressDialog()
            return true
        }

        override fun shouldOverrideUrlLoading(webView: WebView, url: String): Boolean {
            activity.showProgressDialog()
            webView.loadUrl(url)
            activity.dismissProgressDialog()
            return true
        }

        override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
            Toast.makeText(activity, "Got Error! $error", Toast.LENGTH_SHORT).show()
        }
    }
}