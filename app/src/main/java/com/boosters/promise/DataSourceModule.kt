package com.boosters.promise

import android.content.Context
import androidx.room.Room
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {

    private const val PROMISE_DATABASE = "PROMISE_Database"

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return Gson()
    }

    @Provides
    @Singleton
    fun providePromiseLocalDatabase(
        @ApplicationContext appContext: Context,
        gson: Gson
    ): PromiseDatabase {
        return Room.databaseBuilder(appContext, PromiseDatabase::class.java, PROMISE_DATABASE)
            .addTypeConverter(UserTypeConverter(gson))
            .build()
    }

    @Provides
    @Singleton
    fun providePromiseRemoteDatabase(): FirebaseDatabase {
        return Firebase.database("https://promise-c3e7d-default-rtdb.firebaseio.com/")
    }

}