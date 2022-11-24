package com.boosters.promise.data.friend.di

import android.content.Context
import androidx.room.Room
import com.boosters.promise.data.database.UserDataBase
import com.boosters.promise.data.friend.FriendRepository
import com.boosters.promise.data.friend.FriendRepositoryImpl
import com.boosters.promise.data.friend.source.local.FriendLocalDataSource
import com.boosters.promise.data.friend.source.local.FriendLocalDataSourceImpl
import com.boosters.promise.data.friend.source.local.FriendDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FriendDataModule {

    const val USER = "user"

    @Singleton
    @Provides
    fun provideUserDao(userDataBase: UserDataBase): FriendDao {
        return userDataBase.userDao()
    }

    @Singleton
    @Provides
    fun provideUserDatabase(@ApplicationContext applicationContext: Context): UserDataBase {
        return Room.databaseBuilder(
            applicationContext,
            UserDataBase::class.java,
            USER
        ).build()
    }

    @Singleton
    @Provides
    fun provideFriendLocalDataSource(friendDao: FriendDao): FriendLocalDataSource {
        return FriendLocalDataSourceImpl(friendDao)
    }

    @Singleton
    @Provides
    fun provideFriendRepository(friendLocalDataSource: FriendLocalDataSource): FriendRepository {
        return FriendRepositoryImpl(friendLocalDataSource)
    }

}