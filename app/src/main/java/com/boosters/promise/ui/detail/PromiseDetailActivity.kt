package com.boosters.promise.ui.detail

import android.Manifest.permission
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.CompoundButton.OnCheckedChangeListener
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boosters.promise.R
import com.boosters.promise.data.location.GeoLocation
import com.boosters.promise.databinding.ActivityPromiseDetailBinding
import com.boosters.promise.service.locationupload.LocationUploadForegroundService
import com.boosters.promise.service.locationupload.LocationUploadForegroundService.Companion.ACTION_LOCATION_UPLOAD_SERVICE_START
import com.boosters.promise.service.locationupload.LocationUploadForegroundService.Companion.ACTION_LOCATION_UPLOAD_SERVICE_STOP
import com.boosters.promise.service.locationupload.LocationUploadForegroundService.Companion.DELAY_UNTIL_END_TIME_KEY
import com.boosters.promise.service.locationupload.LocationUploadForegroundService.Companion.ID_KEY
import com.boosters.promise.ui.detail.adapter.PromiseMemberAdapter
import com.boosters.promise.ui.detail.model.MemberUiModel
import com.boosters.promise.ui.detail.model.PromiseUploadUiState
import com.boosters.promise.ui.detail.util.MapManager
import com.boosters.promise.ui.loading.LoadingDialog
import com.boosters.promise.ui.promisesetting.PromiseSettingActivity
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class PromiseDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityPromiseDetailBinding

    @Inject
    lateinit var promiseDetailViewModelFactory: PromiseDetailViewModel.PromiseDetailViewModelFactory
    private val promiseDetailViewModel: PromiseDetailViewModel by viewModels {
        PromiseDetailViewModel.provideFactory(
            promiseDetailViewModelFactory,
            intent.getStringExtra(PROMISE_ID_KEY) ?: throw NullPointerException()
        )
    }

    private val promiseMemberAdapter = PromiseMemberAdapter()

    private val onLocationSharingPermissionChanged = OnCheckedChangeListener { _, isChecked ->
        promiseDetailViewModel.updateLocationSharingPermission(isChecked)
    }

    private val onCurrentLocationButtonClickListener = View.OnClickListener {
        if (isLocationPermissionGranted()) {
            mapManager.moveToLocation(promiseDetailViewModel.currentGeoLocation.value)
            return@OnClickListener
        }
        showRequireLocationPermissionSnackBar()
    }

    private lateinit var mapManager: MapManager
    private val destinationMarker = Marker()

    private val locationPermissions = arrayOf(
        permission.ACCESS_COARSE_LOCATION,
        permission.ACCESS_FINE_LOCATION
    )
    private val requestLocationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            if (isLocationPermissionGranted(it.values.toList()) && promiseDetailViewModel.isStartLocationUpdates.value.not()) {
                promiseDetailViewModel.startLocationUpdates()
            }
        }

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_promise_detail)

        loadingDialog = LoadingDialog(this)
        loadingDialog.show()

        setBinding()
        setDestinationButtonClickListener()
        sendPromiseUploadInfoToReceiver()

        initMap()
        setSupportActionBar(binding.toolbarPromiseDetail)
        supportActionBar?.apply {
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }

        setNetworkConnectionObserver()
        promiseDetailViewModel.checkNetworkConnection()
    }

    override fun onStart() {
        super.onStart()
        if (isLocationPermissionGranted()) {
            promiseDetailViewModel.startLocationUpdates()
        } else {
            requestPermission()
        }
    }

    override fun onStop() {
        super.onStop()
        if (promiseDetailViewModel.isStartLocationUpdates.value) promiseDetailViewModel.stopLocationUpdates()
    }

    override fun onMapReady(map: NaverMap) {
        map.addOnLoadListener {
            loadingDialog.dismiss()
        }
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
                if (!promiseDetailViewModel.checkNetworkConnection()) return false
                lifecycleScope.launch {
                    val intent = Intent(
                        this@PromiseDetailActivity,
                        PromiseSettingActivity::class.java
                    ).putExtra(
                        PromiseSettingActivity.PROMISE_KEY,
                        promiseDetailViewModel.promise.first()
                    )
                    startActivity(intent)
                }
            }
            R.id.item_detail_delete -> {
                if (!promiseDetailViewModel.checkNetworkConnection()) return false
                showDeleteDialog()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setBinding() {
        binding.lifecycleOwner = this
        binding.recyclerViewPromiseDetailMemberList.adapter = promiseMemberAdapter
        lifecycleScope.launch {
            launch {
                promiseDetailViewModel.promise.collectLatest {
                    binding.promise = it
                }
            }
            binding.isAcceptLocationSharing =
                promiseDetailViewModel.isAcceptLocationSharing.first().getOrElse { false }
        }

        binding.onLocationSharingPermissionChangedListener = onLocationSharingPermissionChanged
        binding.onCurrentLocationButtonClickListener = onCurrentLocationButtonClickListener
    }

    private fun initMap() {
        val fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.fragment_promiseDetail_map) as MapFragment?
            ?: MapFragment.newInstance().also {
                fm.beginTransaction().add(R.id.fragment_promiseDetail_map, it).commit()
            }
        mapFragment.getMapAsync(this)
    }

    private fun setDestinationButtonClickListener() {
        binding.imageButtonPromiseDetailDestination.setOnClickListener {
            lifecycleScope.launch {
                promiseDetailViewModel.promise.first().let { promise ->
                    mapManager.moveToLocation(promise.destinationGeoLocation)
                }
            }
        }

        binding.imageButtonPromiseDetailMapOverView.setOnClickListener {
            lifecycleScope.launch {
                promiseDetailViewModel.promise.first().let { promise ->
                    overviewMemberLocation(promise.destinationGeoLocation)
                }
            }
        }

        promiseMemberAdapter.setOnItemClickListener(object :
                PromiseMemberAdapter.OnItemClickListener {
                override fun onItemClick(memberUiModel: MemberUiModel) {
                    lifecycleScope.launch {
                        promiseDetailViewModel.userGeoLocations.first().let { userGeoLocation ->
                            val selectedMemberLocation =
                                userGeoLocation.find { it.userCode == memberUiModel.userCode }?.geoLocation

                            if (selectedMemberLocation != null) {
                                mapManager.moveToLocation(selectedMemberLocation)
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
                promiseDetailViewModel.promise.collect { promise ->
                    val destinationLocation = promise.destinationGeoLocation

                    mapManager.markDestination(destinationLocation, destinationMarker)
                    initCameraPosition(destinationLocation)
                    checkArrival()
                }
            }
        }
        markUsersLocationOnMap()

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                promiseDetailViewModel.currentGeoLocation.collectLatest { geoLocation ->
                    mapManager.setCurrentLocation(geoLocation)
                }
            }
        }

        promiseDetailViewModel.isDeleted.observe(this) { isDeleted ->
            if (isDeleted) finish() else showStateSnackbar(R.string.promiseDetail_delete_ask)
        }
    }

    private fun initCameraPosition(destination: GeoLocation) {
        lifecycleScope.launch {
            promiseDetailViewModel.userGeoLocations.first().let {
                mapManager.initCameraPosition(destination, it)
            }
        }
    }

    private fun overviewMemberLocation(destination: GeoLocation?) {
        lifecycleScope.launch {
            val myLocation = promiseDetailViewModel.currentGeoLocation.first()
            val userGeoLocations =
                promiseDetailViewModel.userGeoLocations.first().map { it.geoLocation }
                    .plusElement(myLocation)
            mapManager.overviewMemberLocation(destination, userGeoLocations.filterNotNull())
        }
    }

    private fun markUsersLocationOnMap() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                promiseDetailViewModel.memberMarkerInfo.collectLatest { memberMarkerInfo ->
                    mapManager.updateMemberMarker(memberMarkerInfo)
                }
            }
        }
    }

    private fun checkArrival() {
        lifecycleScope.launch {
            promiseDetailViewModel.memberUiModels.collectLatest { memberUiModels ->
                promiseMemberAdapter.submitList(memberUiModels)
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
            }
            .setNegativeButton(R.string.promiseDetail_dialog_no) { _, _ ->
                return@setNegativeButton
            }
            .create()
            .show()
    }

    private fun isLocationPermissionGranted(): Boolean {
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

    private fun sendPromiseUploadInfoToReceiver() {
        lifecycleScope.launch {
            promiseDetailViewModel.promiseUploadUiState.collectLatest promiseUploadStateCollect@{ promiseUploadUiState ->
                val locationUploadIntent = Intent(this@PromiseDetailActivity, LocationUploadForegroundService::class.java).apply {
                    putExtra(ID_KEY, promiseUploadUiState.id)
                    when (promiseUploadUiState) {
                        is PromiseUploadUiState.Accept -> {
                            if (isLocationPermissionGranted().not()) {
                                binding.switchPromiseDetailLocationSharing.isChecked = false
                                promiseDetailViewModel.stopLocationUpdates()
                                showRequireLocationPermissionSnackBar()
                                return@promiseUploadStateCollect
                            }
                            action = ACTION_LOCATION_UPLOAD_SERVICE_START
                            putExtra(DELAY_UNTIL_END_TIME_KEY, promiseUploadUiState.delayMillisFromCurrentTime)
                        }
                        is PromiseUploadUiState.Denied -> {
                            action = ACTION_LOCATION_UPLOAD_SERVICE_STOP
                        }
                    }
                }
                startForegroundService(locationUploadIntent)
            }
        }
    }

    private fun showRequireLocationPermissionSnackBar() {
        Snackbar.make(
            binding.root,
            R.string.promiseDetail_require_location_permission,
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private fun setNetworkConnectionObserver() {
        lifecycleScope.launch {
            promiseDetailViewModel.networkConnection.collectLatest {
                if (!it) {
                    Snackbar.make(binding.root, R.string.signUp_networkError, Snackbar.LENGTH_SHORT)
                        .show()
                }
            }
        }
    }

    companion object {
        const val PROMISE_ID_KEY = "promiseId"
    }

}