package com.amtgreenberg.thegame.di.module

import android.app.Application
import android.content.Context
import com.amtgreenberg.thegame.di.ViewModelModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(
    includes = [
        ViewModelModule::class
    ]
)
class AppModule(private val application: Application) {

    @Provides
    @Singleton
    fun providesContext(): Context = application
}