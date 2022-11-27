package com.boosters.promise.data.member.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.boosters.promise.data.member.MemberRepository
import com.boosters.promise.data.member.MemberRepositoryImpl
import com.google.firebase.firestore.CollectionReference
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
object MemberModule {

    private const val MEMBERS_PATH = "members"
    private const val LOCATION_SHARING_PERMISSION_PREFERENCES_NAME = "locationSharingPreferences"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MemberCollectionReference

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class LocationSharingPermissionPreferences

    @MemberCollectionReference
    @Singleton
    @Provides
    fun provideMemberCollectionReference(): CollectionReference = Firebase.firestore.collection(MEMBERS_PATH)

    @LocationSharingPermissionPreferences
    @Singleton
    @Provides
    fun provideLocationSharingPermissionPreferences(@ApplicationContext applicationContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler {
                emptyPreferences()
            },
            produceFile = { applicationContext.preferencesDataStoreFile(LOCATION_SHARING_PERMISSION_PREFERENCES_NAME) }
        )
    }

    @Singleton
    @Provides
    fun provideMemberRepository(
        memberRepositoryImpl: MemberRepositoryImpl
    ): MemberRepository = memberRepositoryImpl

}