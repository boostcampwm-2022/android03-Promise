package com.boosters.promise.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

interface NetworkConnectionUtil {

    fun checkNetworkOnline()

}

@Singleton
class NetworkConnectionUtilImpl @Inject constructor(
    @ApplicationContext applicationContext: Context
) : NetworkConnectionUtil {

    private val connectivityManager =
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as? ConnectivityManager

    override fun checkNetworkOnline() {
        val activeNetwork = connectivityManager?.activeNetwork
        val capabilities = connectivityManager?.getNetworkCapabilities(activeNetwork)
        if ((
            capabilities != null && (
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                )
            ).not()
        ) {
            throw NetworkNotOnlineException()
        }
    }

}

class NetworkNotOnlineException : RuntimeException {

    constructor()

    constructor(message: String) : super(message)

}

@Module
@InstallIn(SingletonComponent::class)
object NetworkConnectionUtilModule {

    @Singleton
    @Provides
    fun provideNetworkConnectionUtil(networkConnectionUtilImpl: NetworkConnectionUtilImpl): NetworkConnectionUtil {
        return networkConnectionUtilImpl
    }

}