package dev.jaysonguillen.currencyexchanger.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.jaysonguillen.currencyexchanger.data.repository.RepositoryImpl
import dev.jaysonguillen.currencyexchanger.domain.repository.Repository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun bindsRepository(repositoryImpl: RepositoryImpl): Repository

}