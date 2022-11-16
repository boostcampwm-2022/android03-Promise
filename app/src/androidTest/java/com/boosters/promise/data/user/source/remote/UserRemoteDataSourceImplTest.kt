package com.boosters.promise.data.user.source.remote

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.first
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class UserRemoteDataSourceImplTest {

    private lateinit var userRemoteDataSource: UserRemoteDataSource

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        FirebaseApp.initializeApp(context)
        userRemoteDataSource = UserRemoteDataSourceImpl(Firebase.firestore.collection(USERS_PATH))
    }

    @Test
    fun requestSignUp_SignUpSuccess() = runBlocking {
        val userName = "yang"

        val userCode = userRemoteDataSource.requestSignUp(userName).getOrNull()
        val userBody = userRemoteDataSource.getUserBody(userCode ?: "")

        Assert.assertNotNull(userCode)
        Assert.assertEquals(userName, userBody.first().userName)
    }

    companion object {
        private const val USERS_PATH = "users"
    }

}