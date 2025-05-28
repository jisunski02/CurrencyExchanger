package dev.jaysonguillen.currencyexchanger.core.di

import android.app.Application
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dev.jaysonguillen.currencyexchanger.data.db.AppDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesMovieDatabase(app: Application): AppDatabase{
        return Room.databaseBuilder(app, AppDatabase::class.java,"app_db")
            .fallbackToDestructiveMigration(false)
            .build()
    }
}