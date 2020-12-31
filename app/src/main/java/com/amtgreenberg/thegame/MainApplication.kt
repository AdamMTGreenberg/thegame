package com.amtgreenberg.thegame

import android.app.Application
import com.amtgreenberg.thegame.di.component.AppComponent
import com.amtgreenberg.thegame.di.component.DaggerAppComponent
import com.amtgreenberg.thegame.di.module.AppModule
import com.amtgreenberg.thegame.di.module.LocalStorageModule
import com.facebook.stetho.Stetho
import timber.log.Timber

class MainApplication : Application() {

    companion object {
        lateinit var appComponent: AppComponent
    }

    override fun onCreate() {
        super.onCreate()
        appComponent = initDagger(this)
        initStetho()
        initTimber()
    }

    private fun initDagger(mainApplication: MainApplication): AppComponent {
        return DaggerAppComponent.builder()
            .appModule(AppModule(application = mainApplication))
            .localStorageModule(LocalStorageModule(application = mainApplication))
            .build()
    }

    private fun initStetho() {
        if (BuildConfig.DEBUG) {
            Stetho.initializeWithDefaults(this)
        }
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) { Timber.plant(Timber.DebugTree()) }
    }
}
