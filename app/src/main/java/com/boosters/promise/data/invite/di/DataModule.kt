package com.boosters.promise.data.invite.di

import android.content.Context
import androidx.room.Room
import com.boosters.promise.data.database.UserDataBase
import com.boosters.promise.data.invite.FriendRepository
import com.boosters.promise.data.invite.FriendRepositoryImpl
import com.boosters.promise.data.invite.source.local.FriendLocalDataSource
import com.boosters.promise.data.invite.source.local.FriendLocalDataSourceImpl
import com.boosters.promise.data.invite.source.local.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    const val USER = "user"

    @Singleton
    @Provides
    fun provideUserDao(userDataBase: UserDataBase): UserDao {
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
    fun provideFriendLocalDataSource(userDao: UserDao): FriendLocalDataSource{
        return FriendLocalDataSourceImpl(userDao)
    }

    @Singleton
    @Provides
    fun provideFriendRepository(friendLocalDataSource: FriendLocalDataSource): FriendRepository {
        return FriendRepositoryImpl(friendLocalDataSource)
    }

}