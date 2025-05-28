package dev.jaysonguillen.currencyexchanger.core.di

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.jaysonguillen.currencyexchanger.core.utils.PrefsManager
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {

    private const val PREF_NAME = "my_shared_prefs"

    @Provides
    @Singleton
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePrefsManager(sharedPreferences: SharedPreferences): PrefsManager {
        return PrefsManager(sharedPreferences)
    }
}
