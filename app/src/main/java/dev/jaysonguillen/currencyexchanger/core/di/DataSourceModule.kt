package dev.jaysonguillen.currencyexchanger.core.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.jaysonguillen.currencyexchanger.data.repository.source.LocalSource
import dev.jaysonguillen.currencyexchanger.data.repository.source.LocalSourceImpl
import dev.jaysonguillen.currencyexchanger.data.repository.source.RemoteSource
import dev.jaysonguillen.currencyexchanger.data.repository.source.RemoteSourceImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {
    @Singleton
    @Binds
    abstract fun bindRemoteSource(remoteSourceImpl: RemoteSourceImpl): RemoteSource

    @Singleton
    @Binds
    abstract fun bindLocalSource(localSourceImpl: LocalSourceImpl): LocalSource
}