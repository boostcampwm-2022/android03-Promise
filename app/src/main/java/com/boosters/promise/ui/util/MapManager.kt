package com.boosters.promise.ui.util

import com.boosters.promise.R
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage

class MapManager(val map: NaverMap) {

    fun initCameraPosition(location: LatLng) {
        val cameraUpdate = CameraUpdate.scrollTo(location)
        map.moveCamera(cameraUpdate)
    }

    fun moveCamera(location: LatLng) {
        val cameraUpdate = CameraUpdate.scrollTo(location).animate(CameraAnimation.Easing)
        map.moveCamera(cameraUpdate)
    }

    fun markDestination(location: LatLng, marker: Marker) {
        marker.also {
            it.position = location
            it.map = map
            it.icon = OverlayImage.fromResource(R.drawable.ic_destination_marker)
            it.globalZIndex = DESTINATION_MARKER_Z_INDEX
        }
    }

    fun markMemberLocation(location: LatLng, marker: Marker) {
        marker.also {
            it.position = location
            it.map = map
            it.icon = OverlayImage.fromResource(R.drawable.ic_member_marker)
        }
    }

    fun calculateDistance(location1: LatLng, location2: LatLng): Double {
        return location1.distanceTo(location2)
    }

    companion object {
        const val DESTINATION_MARKER_Z_INDEX = 15000
    }

}