package com.boosters.promise.data.member.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.boosters.promise.data.member.di.MemberModule.LocationSharingPermissionPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationSharingPermissionLocalDataSource @Inject constructor(
    @LocationSharingPermissionPreferences private val locationSharingPermissionPreferences: DataStore<Preferences>
) {

    suspend fun saveLocationSharingPermission(promiseId: String, isLocationSharing: Boolean) {
        locationSharingPermissionPreferences.edit { preferences ->
            preferences[booleanPreferencesKey(promiseId)] = isLocationSharing
        }
    }

    fun getLocationSharingPermission(promiseId: String): Flow<Result<Boolean>> =
        locationSharingPermissionPreferences.data.catch { exception ->
            if (exception is IOException) emit(emptyPreferences())
        }.map { preferences ->
            runCatching {
                preferences[booleanPreferencesKey(promiseId)] ?: throw NullPointerException()
            }
        }

}