package com.sphtech.shared.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.sphtech.shared.BuildConfig
import com.sphtech.shared.core.base.BaseRepository
import com.sphtech.shared.network.repository.DataAmountRepository
import com.sphtech.shared.network.repository.prefs.SharedPreferenceStorage
import com.sphtech.shared.util.ConstantsBase
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.security.SecureRandom
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager
import javax.security.cert.CertificateException

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else HttpLoggingInterceptor.Level.NONE
        }
    }

    @Provides
    @Singleton
    fun provideInterceptor(): Interceptor {
        return Interceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor, interceptor: Interceptor
    ): OkHttpClient {
        val httpClient = OkHttpClient().newBuilder()
            .connectTimeout(ConstantsBase.REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(ConstantsBase.REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(ConstantsBase.REQUEST_TIMEOUT.toLong(), TimeUnit.SECONDS)
        httpClient.addInterceptor(loggingInterceptor)
        httpClient.addInterceptor(interceptor)
        trustAllHosts(httpClient)
        return httpClient.build()
    }

    /**
     * Trust every server - dont check for any certificate
     */
    private fun trustAllHosts(httpClient: OkHttpClient.Builder) {
        // Create a trust manager that does not validate certificate chains
        val trustAllCerts: Array<TrustManager> =
            arrayOf<TrustManager>(object : X509TrustManager {
                /* val acceptedIssuers: Array<X509Certificate?>?
                     get() = arrayOf()
 */
                @Throws(CertificateException::class)
                override fun checkClientTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(
                    chain: Array<X509Certificate?>?,
                    authType: String?
                ) {
                }

                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }
            })

        // Install the all-trusting trust manager
        try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(null, trustAllCerts, SecureRandom())
            val sslSocketFactory = sslContext.getSocketFactory();
            HttpsURLConnection
                .setDefaultSSLSocketFactory(sslContext.socketFactory)
            httpClient.sslSocketFactory(sslSocketFactory, trustAllCerts[0] as X509TrustManager);
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }


    @Provides
    @Singleton
    fun provideRetrofit(okHttp: OkHttpClient): Retrofit.Builder {
        return Retrofit.Builder()
            .client(okHttp)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
    }

    @Provides
    @Singleton
    fun provideDataAmountRepository(
        apiService: ApiService,
        baseRepository: BaseRepository
    ): DataAmountRepository = DataAmountRepository(apiService, baseRepository)
}
