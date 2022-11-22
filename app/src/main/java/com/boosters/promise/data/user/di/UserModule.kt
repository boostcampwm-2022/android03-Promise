package com.boosters.promise.data.user.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.data.user.UserRepositoryImpl
import com.boosters.promise.data.user.source.local.MyInfoLocalDataSource
import com.boosters.promise.data.user.source.local.MyInfoLocalDataSourceImpl
import com.boosters.promise.data.user.source.remote.UserRemoteDataSource
import com.boosters.promise.data.user.source.remote.UserRemoteDataSourceImpl
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UserModule {

    private const val USERS_PATH = "users"
    private const val MY_INFO_PREFERENCES_NAME = "myInfoPreferences"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UserCollectionReference

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MyInfoPreferencesDataStore

    @UserCollectionReference
    @Singleton
    @Provides
    fun provideUserCollectionReference(): CollectionReference = Firebase.firestore.collection(USERS_PATH)

    @MyInfoPreferencesDataStore
    @Singleton
    @Provides
    fun provideMyInfoPreferencesDataStore(@ApplicationContext applicationContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler {
                emptyPreferences()
            },
            produceFile = { applicationContext.preferencesDataStoreFile(MY_INFO_PREFERENCES_NAME) }
        )
    }

    @Singleton
    @Provides
    fun provideUserRemoteDataSource(userRemoteDataSourceImpl: UserRemoteDataSourceImpl): UserRemoteDataSource = userRemoteDataSourceImpl

    @Singleton
    @Provides
    fun provideUserRepository(userRepositoryImpl: UserRepositoryImpl): UserRepository = userRepositoryImpl
    @Singleton
    @Provides
    fun provideMyInfoLocalDataSource(myInfoLocalDataSourceImpl: MyInfoLocalDataSourceImpl): MyInfoLocalDataSource = myInfoLocalDataSourceImpl

}