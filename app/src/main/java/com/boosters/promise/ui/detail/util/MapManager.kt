package com.boosters.promise.ui.detail.util

import com.boosters.promise.R
import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.location.toLatLng
import com.boosters.promise.ui.detail.model.MemberUiModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

class MapManager(val map: NaverMap) {

    init {
        map.maxZoom = MAP_MAX_ZOOM_LEVEL
    }

    fun initCameraPosition(destination: GeoLocation, users: List<MemberUiModel>) {
        val bound = calculateBound(destination, users.map { it.geoLocation })

        val cameraUpdate = CameraUpdate.fitBounds(bound, MAP_OVERVIEW_PADDING)
        map.moveCamera(cameraUpdate)
    }

    fun moveToLocation(location: GeoLocation) {
        val cameraUpdate =
            CameraUpdate.scrollAndZoomTo(location.toLatLng(), LOCATION_ZOOM_LEVEL).animate(
                CameraAnimation.Easing, MAP_ANIMATION_DURATION
            )

        map.moveCamera(cameraUpdate)
    }

    fun overviewMemberLocation(destination: GeoLocation, users: List<MemberUiModel>) {
        val bound = calculateBound(destination, users.map { it.geoLocation })

        val cameraUpdate =
            CameraUpdate.fitBounds(bound, MAP_OVERVIEW_PADDING)
                .animate(CameraAnimation.Easing, MAP_ANIMATION_DURATION)
        map.moveCamera(cameraUpdate)
    }

    fun markDestination(location: GeoLocation, marker: Marker) {
        marker.apply {
            position = location.toLatLng()
            map = this@MapManager.map
            icon = OverlayImage.fromResource(R.drawable.ic_destination_marker)
            globalZIndex = DESTINATION_MARKER_Z_INDEX
        }
    }

    fun markMemberLocation(userName: String, location: GeoLocation, marker: Marker) {
        marker.apply {
            position = location.toLatLng()
            map = this@MapManager.map
            icon = OverlayImage.fromResource(R.drawable.ic_member_marker)
            captionText = userName
            captionTextSize = 15.0f
            setCaptionAligns(Align.Top)
        }
    }

    private fun calculateBound(
        destination: GeoLocation,
        locations: List<GeoLocation?>
    ): LatLngBounds {
        val memberLocations = locations.filterNotNull().plus(destination)

        val southWest = LatLng(
            memberLocations.minOf { it.latitude },
            memberLocations.minOf { it.longitude }
        )
        val northEast = LatLng(
            memberLocations.maxOf { it.latitude },
            memberLocations.maxOf { it.longitude }
        )

        return LatLngBounds(southWest, northEast)
    }

    companion object {
        const val DESTINATION_MARKER_Z_INDEX = 15000
        const val MAP_MAX_ZOOM_LEVEL = 16.0
        const val MAP_OVERVIEW_PADDING = 200
        const val LOCATION_ZOOM_LEVEL = 14.0
        const val MAP_ANIMATION_DURATION = 2000L
    }

}