package com.amtgreenberg.thegame.di.module

import com.amtgreenberg.thegame.datasource.local.RaffleDao
import com.amtgreenberg.thegame.di.IoDispatcher
import com.amtgreenberg.thegame.repository.DefaultRaffleRepository
import com.amtgreenberg.thegame.repository.RaffleRepository
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
object RepositoryModule {

    @[Singleton Provides JvmStatic]
    fun provideRaffleRepository(
        raffleDao: RaffleDao,
        @IoDispatcher ioDispatcher: CoroutineDispatcher
    ): RaffleRepository = DefaultRaffleRepository(raffleDao, ioDispatcher)
}