package com.boosters.promise.data.user.di

import android.content.Context
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.data.user.UserRepositoryImpl
import com.boosters.promise.data.user.source.local.MyInfoLocalDataSource
import com.boosters.promise.data.user.source.local.MyInfoLocalDataSourceImpl
import com.boosters.promise.data.user.source.remote.UserRemoteDataSource
import com.boosters.promise.data.user.source.remote.UserRemoteDataSourceImpl
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    private const val USERS_PATH = "users"
    private const val MY_INFO_PREFERENCES_NAME = "myInfoPreferences"

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(): UserRemoteDataSource =
        UserRemoteDataSourceImpl(Firebase.firestore.collection(USERS_PATH))

    @Singleton
    @Provides
    fun provideUserRepository(
        userRemoteDataSource: UserRemoteDataSource,
        myInfoLocalDataSource: MyInfoLocalDataSource
    ): UserRepository {
        return UserRepositoryImpl(userRemoteDataSource, myInfoLocalDataSource)
    }

    @Singleton
    @Provides
    fun provideMyInfoLocalDataSource(@ApplicationContext applicationContext: Context): MyInfoLocalDataSource {
        val myInfoPreferencesDataStore = PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler {
                emptyPreferences()
            },
            produceFile = { applicationContext.preferencesDataStoreFile(MY_INFO_PREFERENCES_NAME) }
        )
        return MyInfoLocalDataSourceImpl(myInfoPreferencesDataStore)
    }

}