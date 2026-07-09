package com.example.matchcast.di

import android.content.Context
import com.example.matchcast.data.local.AppDatabase
import com.example.matchcast.data.local.MatchDao
import com.example.matchcast.data.repository.MatchRepositoryImpl
import com.example.matchcast.domain.repository.MatchRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase{
        return AppDatabase.getDatabase(context)
    }

    @Provides
    fun provideDao(database: AppDatabase): MatchDao{
        return database.matchDao()
    }

    @Provides
    fun provideRepository(impl: MatchRepositoryImpl): MatchRepository{
        return impl
    }
}