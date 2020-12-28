package com.amtgreenberg.thegame.di.component

import com.amtgreenberg.thegame.di.module.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class
    ]
)
interface AppComponent
