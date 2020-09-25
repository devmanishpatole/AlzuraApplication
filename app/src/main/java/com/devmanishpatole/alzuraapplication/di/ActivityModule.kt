package com.devmanishpatole.alzuraapplication.di

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import com.devmanishpatole.alzuraapplication.orders.adapter.OrderListAdapter
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object ActivityModule {

    @Provides
    fun provideCharacterPagingAdapter(@ActivityContext appContext: Context) =
        OrderListAdapter((appContext as AppCompatActivity).lifecycle)

}