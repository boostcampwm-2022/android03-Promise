package com.boosters.promise.di

import android.content.Context
import androidx.room.Room
import com.boosters.promise.data.database.UserDataBase
import com.boosters.promise.data.invite.FriendRepository
import com.boosters.promise.data.invite.FriendRepositoryImpl
import com.boosters.promise.data.invite.source.local.FriendLocalDataSource
import com.boosters.promise.data.invite.source.local.FriendLocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideUserDatabase(@ApplicationContext applicationContext: Context): UserDataBase {
        return Room.databaseBuilder(
            applicationContext,
            UserDataBase::class.java,
            "user"
        ).build()
    }

    @Singleton
    @Provides
    fun provideFriendLocalDataSource(userDataBase: UserDataBase): FriendLocalDataSource{
        return FriendLocalDataSourceImpl(userDataBase)
    }

    @Singleton
    @Provides
    fun provideFriendRepository(friendLocalDataSource: FriendLocalDataSource): FriendRepository {
        return FriendRepositoryImpl(friendLocalDataSource)
    }

}