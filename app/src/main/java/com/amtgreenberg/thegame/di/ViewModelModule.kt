package com.amtgreenberg.thegame.di

import androidx.lifecycle.ViewModelProvider
import dagger.Module

/**
 * Module used to define the connection between the framework's [ViewModelProvider.Factory] and
 * our own implementation: [DaggerViewModelFactory].
 */
@Module
abstract class ViewModelModule {
}