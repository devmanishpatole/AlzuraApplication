package com.devmanishpatole.alzuraapplication.di

import com.devmanishpatole.alzuraapplication.login.service.LoginService
import com.devmanishpatole.alzuraapplication.orders.service.OrderService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object ServiceModule {

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): LoginService =
        retrofit.create(LoginService::class.java)

    @Provides
    @Singleton
    fun provideOrderService(retrofit: Retrofit): OrderService =
        retrofit.create(OrderService::class.java)

}