package com.devmanishpatole.alzuraapplication.di

import android.content.Context
import com.devmanishpatole.alzuraapplication.BuildConfig.BASE_URL
import com.devmanishpatole.alzuraapplication.login.data.SessionManager
import com.devmanishpatole.alzuraapplication.network.Networking
import com.devmanishpatole.alzuraapplication.util.NetworkHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    private const val ACCEPT = "Accept"
    private const val ACCEPT_VALUE = "application/vnd.saitowag.api+json;version=1.0"
    private const val AUTH_TOKEN = "X-AUTH-TOKEN"

    @Provides
    @Singleton
    fun provideInterceptor() = object : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val original = chain.request()

            val builder = original.newBuilder()
                .header(
                    ACCEPT,
                    ACCEPT_VALUE
                )
            if (SessionManager.getToken().isNotEmpty()) {
                builder.header(
                    AUTH_TOKEN,
                    SessionManager.getToken()
                )
            }
            val request = builder.build()
            return chain.proceed(request)
        }
    }


    @Provides
    @Singleton
    fun provideLoggingInterceptor() = HttpLoggingInterceptor()
        .apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

    @Provides
    @Singleton
    fun provideNetwork(
        @ApplicationContext appContext: Context,
        loggingInterceptor: HttpLoggingInterceptor,
        interceptor: Interceptor
    ): Retrofit =
        Networking.create(
            BASE_URL,
            appContext.cacheDir,
            10 * 1024 * 1024, // 10MB
            loggingInterceptor,
            interceptor
        )

    @Provides
    @Singleton
    fun provideNetworkHelper(@ApplicationContext appContext: Context) = NetworkHelper(appContext)

}