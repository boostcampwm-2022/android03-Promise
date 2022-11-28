package com.boosters.promise.ui.util

import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker

class MapManager(val map: NaverMap) {

    fun initCameraPosition(location: LatLng) {
        val cameraUpdate = CameraUpdate.scrollTo(location)
        map.moveCamera(cameraUpdate)
    }

    fun moveCamera(location: LatLng) {
        val cameraUpdate = CameraUpdate.scrollTo(location).animate(CameraAnimation.Easing)
        map.moveCamera(cameraUpdate)
    }

    fun markLocation(location: LatLng, marker: Marker) {
        marker.also {
            it.position = location
            it.map = map
        }
    }

    fun markLocation(location: LatLng, marker: Marker, markerColor: Int) {
        marker.also {
            it.iconTintColor = markerColor
            it.position = location
            it.map = map
        }
    }

    fun calculateDistance(location1: LatLng, location2: LatLng): Double {
        return location1.distanceTo(location2)
    }

}