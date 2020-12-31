package com.amtgreenberg.thegame.di.module

import android.app.Application
import com.amtgreenberg.thegame.datasource.local.RaffleDao
import com.amtgreenberg.thegame.datasource.local.RaffleDb
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class LocalStorageModule(private val application: Application) {

    private var db: RaffleDb = RaffleDb.getDatabase(application)
//        Room.databaseBuilder(application, RaffleDb::class.java, "db").build()

    @[Singleton Provides]
    fun provideRaffleDatabase(): RaffleDb = db

    @[Singleton Provides]
    fun provideRaffleDao(daoDb: RaffleDb): RaffleDao = daoDb.raffleDao()

}