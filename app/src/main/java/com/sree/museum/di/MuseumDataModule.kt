package com.sree.museum.di

import com.sree.museum.data.MuseumRepoImpl
import com.sree.museum.data.api.MuseumApi
import com.sree.museum.data.api.MuseumItemSearchApi
import com.sree.museum.domain.MuseumRepo
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class MuseumDataModule {

    @Binds
    @Singleton
    abstract fun bindMuseumRepo(museumRepoImpl: MuseumRepoImpl): MuseumRepo

    companion object {
        @Provides
        @Singleton
        fun provideMuseumApi(retrofit: Retrofit): MuseumApi {
            return retrofit.create(MuseumApi::class.java)
        }

        @Provides
        @Singleton
        fun provideMuseumSearchApi(retrofit: Retrofit) : MuseumItemSearchApi {
            return retrofit.create(MuseumItemSearchApi::class.java)
        }
    }
}