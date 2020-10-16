package bibou.biboubeauty.com.utils.networking

import android.content.Context
import android.util.Log
import com.e5ctech.wtfsports.BuildConfig
import com.e5ctech.wtfsports.accounts.api.DashboardApi
import com.e5ctech.wtfsports.accounts.api.UsersApi
import com.e5ctech.wtfsports.utils.base.BaseActivity
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class BibouApiClient(var context: Context) {

    private val retrofit: retrofit2.Retrofit

    val usersApi: UsersApi
        get() = retrofit.create(UsersApi::class.java)

    val dashboardApi: DashboardApi
        get() = retrofit.create(DashboardApi::class.java)

    init {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(100000, TimeUnit.SECONDS)
            .readTimeout(100000, TimeUnit.SECONDS)
            .writeTimeout(100000, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder: Request.Builder
                val token = baseActivity!!.getUsersLocally().tokens.access
                if(token.isNotEmpty()){
                    requestBuilder = original.newBuilder()
                        .addHeader("Content-Type", "application/json;charset=UTF-8")
                        .addHeader("Authorization", " Bearer " + baseActivity!!.getUsersLocally().tokens.access)
                        .method(original.method(), original.body())
                    Log.e("ttookkeenn","::::" + baseActivity!!.getUsersLocally().tokens.access)
                }else{
                    requestBuilder = original.newBuilder()
                        .addHeader("Content-Type", "application/json;charset=UTF-8")
                        .method(original.method(), original.body())
                }

                val request = requestBuilder.build()
                chain.proceed(request)
            }.build()

        retrofit = retrofit2.Retrofit.Builder()
            .baseUrl(BuildConfig.APP_HOST)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(okHttpClient)
            .build()
    }

    companion object {
        private var mInstance: BibouApiClient? = null
        private var baseActivity: BaseActivity? = null

        fun instance(baseActivity: BaseActivity): BibouApiClient =
            mInstance ?: synchronized(this) {
                mInstance ?: BibouApiClient(baseActivity).also {
                    mInstance = it
                    this.baseActivity = baseActivity
                }
            }
    }

}
