package com.boosters.promise.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boosters.promise.R
import com.boosters.promise.data.location.toLatLng
import com.boosters.promise.databinding.ActivityPromiseDetailBinding
import com.boosters.promise.ui.detail.adapter.PromiseMemberAdapter
import com.boosters.promise.ui.promisecalendar.PromiseCalendarActivity
import com.boosters.promise.ui.promisesetting.PromiseSettingActivity
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import android.Manifest.permission
import android.content.pm.PackageManager
import com.boosters.promise.data.user.User
import com.boosters.promise.service.locationupload.LocationUploadForegroundService
import com.boosters.promise.service.locationupload.LocationUploadServiceConnection
import com.boosters.promise.ui.util.MapManager
import com.naver.maps.geometry.LatLng

@AndroidEntryPoint
class PromiseDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityPromiseDetailBinding
    private val promiseDetailViewModel: PromiseDetailViewModel by viewModels()
    private val promiseMemberAdapter = PromiseMemberAdapter()
    private lateinit var mapManager: MapManager
    private val destinationMarker = Marker()

    private val locationPermissions = arrayOf(
        permission.ACCESS_COARSE_LOCATION,
        permission.ACCESS_FINE_LOCATION
    )
    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val locationPermissionCheckResult = locationPermissions.map {
                permissions.getOrDefault(it, false)
            }
            if (isLocationPermissionGranted(locationPermissionCheckResult)) {
                startLocationUploadService()
            }
        }

    private val locationUploadServiceConnection = LocationUploadServiceConnection()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_promise_detail)
        setBinding()

        initMap()
        setPromiseInfo()
        setListener()

        setSupportActionBar(binding.toolbarPromiseDetail)
        supportActionBar?.apply {
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        promiseDetailViewModel.isDeleted.observe(this) {
            if (it) {
                finish()
            } else {
                showStateSnackbar(R.string.promiseDetail_delete_ask)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if (checkLocationPermission()) startLocationUploadService() else requestPermission()
    }

    override fun onStop() {
        super.onStop()
        unbindService(locationUploadServiceConnection)
    }

    override fun onMapReady(map: NaverMap) {
        mapManager = MapManager(map)
        setObserver()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_detail_edit -> {
                val intent = Intent(this, PromiseSettingActivity::class.java).putExtra(
                    PromiseSettingActivity.PROMISE_KEY,
                    promiseDetailViewModel.promiseInfo.value
                )
                startActivity(intent)
            }
            R.id.item_detail_delete -> {
                showDeleteDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBinding() {
        binding.lifecycleOwner = this
        binding.viewModel = promiseDetailViewModel
        binding.recyclerViewPromiseDetailMemberList.adapter = promiseMemberAdapter
    }

    private fun setPromiseInfo() {
        intent.getStringExtra(PROMISE_ID_KEY)?.let { promiseId ->
            promiseDetailViewModel.setPromiseInfo(promiseId)
        }
    }

    private fun initMap() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.fragment_promiseDetail_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.fragment_promiseDetail_map, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    private fun setListener() {
        binding.imageButtonPromiseDetailDestination.setOnClickListener {
            lifecycleScope.launch {
                promiseDetailViewModel.promiseInfo.collectLatest { promise ->
                    mapManager.moveCamera(promise.destinationGeoLocation.toLatLng())
                }
            }
        }

        promiseMemberAdapter.setOnItemClickListener(object :
            PromiseMemberAdapter.OnItemClickListener {
            override fun onItemClick(user: User, position: Int) {
                lifecycleScope.launch {
                    promiseDetailViewModel.memberLocations.collectLatest {
                        val selectedMemberPosition = it[position]

                        if (selectedMemberPosition != null) {
                            mapManager.moveCamera(selectedMemberPosition.toLatLng())
                        } else {
                            showStateSnackbar(R.string.promiseDetail_memberLocation_null)
                        }
                    }
                }
            }
        })
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                promiseDetailViewModel.promiseInfo.collectLatest { promise ->
                    launch {
                        promiseMemberAdapter.submitList(promise.members)
                    }

                    launch {
                        val destinationLocation = promise.destinationGeoLocation.toLatLng()

                        mapManager.initCameraPosition(destinationLocation)
                        mapManager.markDestination(destinationLocation, destinationMarker)
                        markUsersLocationOnMap()
                        checkArrival(destinationLocation)
                    }
                }
            }
        }
    }

    private suspend fun markUsersLocationOnMap() {
        lifecycleScope.launch {
            promiseDetailViewModel.memberLocations.collectLatest {
                it.forEachIndexed { idx, memberLocation ->
                    if (memberLocation != null) {
                        promiseDetailViewModel.memberMarkers[idx].apply {
                            mapManager.markMemberLocation(memberLocation.toLatLng(), this)
                        }
                    }
                }
            }
        }
    }

    private fun checkArrival(destination: LatLng) {
        lifecycleScope.launch {
            promiseDetailViewModel.memberLocations.collectLatest {
                it.forEachIndexed { idx, memberLocation ->
                    if (memberLocation != null) {
                        promiseDetailViewModel.memberMarkers[idx].apply {
                            val distance =
                                mapManager.calculateDistance(destination, memberLocation.toLatLng())

                            if (distance < MINIMUM_ARRIVE_DISTANCE) {
                                promiseMemberAdapter.arrivedMember = idx
                                promiseMemberAdapter.notifyItemChanged(idx)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showStateSnackbar(message: Int) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setMessage(R.string.promiseDetail_delete_ask)
            .setPositiveButton(R.string.promiseDetail_dialog_yes) { _, _ ->
                promiseDetailViewModel.removePromise()
                startActivity(
                    Intent(this, PromiseCalendarActivity::class.java)
                ).also { finish() }
            }
            .setNegativeButton(R.string.promiseDetail_dialog_no) { _, _ ->
                return@setNegativeButton
            }
            .create()
            .show()
    }

    private fun startLocationUploadService() {
        val intent = Intent(this, LocationUploadForegroundService::class.java).apply {
            putExtra(LocationUploadForegroundService.END_TIME_KEY, DEFAULT_LOCATION_UPLOAD_END_TIME)
        }
        bindService(intent, locationUploadServiceConnection, BIND_AUTO_CREATE)
    }

    private fun checkLocationPermission(): Boolean {
        val locationPermissionCheckResult = locationPermissions.map {
            checkSelfPermission(it) == PackageManager.PERMISSION_GRANTED
        }
        return isLocationPermissionGranted(locationPermissionCheckResult)
    }

    private fun requestPermission() {
        requestLocationPermissionLauncher.launch(locationPermissions)
    }

    private fun isLocationPermissionGranted(permissionCheckResult: List<Boolean>): Boolean {
        return permissionCheckResult.fold(false) { acc, locationPermission ->
            acc || locationPermission
        }
    }

    companion object {
        const val PROMISE_ID_KEY = "promiseId"
        private const val DEFAULT_LOCATION_UPLOAD_END_TIME = 90_000L
        private const val MINIMUM_ARRIVE_DISTANCE = 50
    }

}