package com.boosters.promise.data.member.di

import com.boosters.promise.data.member.MemberRepository
import com.boosters.promise.data.member.MemberRepositoryImpl
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MemberModule {

    private const val MEMBERS_PATH = "members"

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class MemberCollectionReference

    @MemberCollectionReference
    @Singleton
    @Provides
    fun provideMemberCollectionReference(): CollectionReference = Firebase.firestore.collection(MEMBERS_PATH)

    @Singleton
    @Provides
    fun provideMemberRepository(
        memberRepositoryImpl: MemberRepositoryImpl
    ): MemberRepository = memberRepositoryImpl

}