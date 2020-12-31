package com.amtgreenberg.thegame.di.component

import com.amtgreenberg.thegame.di.module.AppModule
import com.amtgreenberg.thegame.di.module.LocalStorageModule
import com.amtgreenberg.thegame.di.module.RepositoryModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AppModule::class,
        RepositoryModule::class,
        LocalStorageModule::class
    ]
)
interface AppComponent
