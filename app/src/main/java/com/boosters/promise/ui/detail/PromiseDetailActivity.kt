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
import android.content.IntentFilter
import android.content.pm.PackageManager
import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.data.user.toMemberUiModel
import com.boosters.promise.ui.detail.util.MapManager
import com.boosters.promise.receiver.LocationUploadReceiver
import com.boosters.promise.ui.detail.model.PromiseUploadState

@AndroidEntryPoint
class PromiseDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityPromiseDetailBinding
    private val promiseDetailViewModel: PromiseDetailViewModel by viewModels()
    private val promiseMemberAdapter = PromiseMemberAdapter()

    private lateinit var mapManager: MapManager
    private val destinationMarker = Marker()

    private val locationUploadReceiver = LocationUploadReceiver()

    private val locationPermissions = arrayOf(
        permission.ACCESS_COARSE_LOCATION,
        permission.ACCESS_FINE_LOCATION
    )
    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_promise_detail)
        setBinding()

        if (checkLocationPermission().not()) requestPermission()
        registerLocationUploadReceiver()
        sendPromiseUploadInfoToReceiver()

        initMap()
        setPromiseInfo()
        setListener()

        setSupportActionBar(binding.toolbarPromiseDetail)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }

        promiseDetailViewModel.isDeleted.observe(this) {
            if (it) {
                finish()
            } else {
                showStateSnackbar(R.string.promiseDetail_delete_ask)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(locationUploadReceiver)
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
                finish()
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
                    mapManager.moveToLocation(promise?.destinationGeoLocation)
                }
            }
        }

        binding.imageButtonPromiseDetailMapOverView.setOnClickListener {
            lifecycleScope.launch {
                promiseDetailViewModel.promiseInfo.collectLatest { promise ->
                    overviewMemberLocation(promise?.destinationGeoLocation)
                }
            }
        }

        promiseMemberAdapter.setOnItemClickListener(object : PromiseMemberAdapter.OnItemClickListener {
            override fun onItemClick(position: Int) {
                lifecycleScope.launch {
                    promiseDetailViewModel.memberLocations.collectLatest {
                        val selectedMember = it?.get(position)

                        if (selectedMember?.geoLocation != null) {
                            mapManager.moveToLocation(selectedMember.geoLocation)
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
                promiseDetailViewModel.promiseInfo.collect { promise ->
                    launch {
                        if (promise != null) {
                            promiseMemberAdapter.submitList(
                                promise.members.map {
                                    it.toMemberUiModel()
                                }
                            )
                        }
                    }

                    launch {
                        if (promise != null) {
                            val destinationLocation = promise.destinationGeoLocation

                            mapManager.markDestination(destinationLocation, destinationMarker)
                            markUsersLocationOnMap()
                            initCameraPosition(destinationLocation)
                            checkArrival(destinationLocation)
                        }
                    }
                }
            }
        }
    }

    private fun initCameraPosition(destination: GeoLocation) {
        lifecycleScope.launch {
            promiseDetailViewModel.memberLocations.collectLatest {
                if (it != null) {
                    mapManager.initCameraPosition(destination, it)
                }
            }
        }
    }

    private fun overviewMemberLocation(destination: GeoLocation?) {
        lifecycleScope.launch {
            promiseDetailViewModel.memberLocations.collectLatest { memberUiModel ->
                if (memberUiModel != null) {
                    mapManager.overviewMemberLocation(destination, memberUiModel)
                }
            }
        }
    }

    private suspend fun markUsersLocationOnMap() {
        lifecycleScope.launch {
            promiseDetailViewModel.memberLocations.collectLatest {
                it?.forEachIndexed { idx, memberUiModel ->
                    if (memberUiModel.geoLocation != null) {
                        promiseDetailViewModel.memberMarkers[idx].apply {
                            mapManager.markMemberLocation(
                                memberUiModel.userName,
                                memberUiModel.geoLocation,
                                this
                            )
                        }
                    }
                }
            }
        }
    }

    private fun checkArrival(destination: GeoLocation) {
        lifecycleScope.launch {
            promiseDetailViewModel.memberLocations.collectLatest {
                if (it != null) {
                    val arriveCheckedList = promiseDetailViewModel.checkArrival(destination, it)
                    promiseMemberAdapter.submitList(arriveCheckedList)
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

    private fun registerLocationUploadReceiver() {
        val intentFilter = IntentFilter().apply {
            addAction(LocationUploadReceiver.ACTION_LOCATION_UPLOAD_SERVICE_START)
            addAction(LocationUploadReceiver.ACTION_LOCATION_UPLOAD_SERVICE_STOP)
        }
        registerReceiver(locationUploadReceiver, intentFilter)
    }

    private fun sendPromiseUploadInfoToReceiver() {
        lifecycleScope.launch {
            promiseDetailViewModel.promiseUploadState.collectLatest promiseUploadStateCollect@{ promiseUploadState ->
                if (promiseUploadState == null) return@promiseUploadStateCollect
                val intent = when (promiseUploadState) {
                    is PromiseUploadState.Accept -> {
                        if (checkLocationPermission().not()) {
                            requestPermission().also { if (checkLocationPermission().not()) return@promiseUploadStateCollect }
                        }
                        Intent(LocationUploadReceiver.ACTION_LOCATION_UPLOAD_SERVICE_START).apply {
                            putExtra(LocationUploadReceiver.PROMISE_DATE_TIME_KEY, promiseUploadState.dateAndTime)
                        }
                    }
                    is PromiseUploadState.Denied -> {
                        Intent(LocationUploadReceiver.ACTION_LOCATION_UPLOAD_SERVICE_STOP)
                    }
                }.apply {
                    putExtra(LocationUploadReceiver.PROMISE_ID_KEY, promiseUploadState.id)
                }
                sendOrderedBroadcast(intent, null)
            }
        }
    }

    companion object {
        const val PROMISE_ID_KEY = "promiseId"
    }

}