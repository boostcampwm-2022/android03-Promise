package com.boosters.promise.ui.detail

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import com.boosters.promise.R
import com.boosters.promise.data.model.Location
import com.boosters.promise.databinding.ActivityPromiseDetailBinding
import com.boosters.promise.ui.detail.adapter.PromiseMemberAdapter
import com.boosters.promise.ui.promisecalendar.PromiseCalendarActivity
import com.boosters.promise.ui.promisesetting.PromiseSettingActivity
import com.boosters.promise.ui.promisesetting.model.PromiseUiState
import com.google.android.material.snackbar.Snackbar
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.Tm128
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PromiseDetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityPromiseDetailBinding
    private val promiseDetailViewModel: PromiseDetailViewModel by viewModels()
    private val promiseMemberAdapter = PromiseMemberAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_promise_detail)
        setBinding()
        initMap()
        setPromiseInfo()

        setSupportActionBar(binding.toolbarPromiseDetail)
        supportActionBar?.apply {
            setDisplayShowCustomEnabled(true)
            setDisplayShowTitleEnabled(false)
            setDisplayHomeAsUpEnabled(true)
        }

        promiseDetailViewModel.isDeleted.observe(this) {
            if (it) {
                startActivity(
                    Intent(this, PromiseCalendarActivity::class.java)
                ).also { finish() }
                finish()
            } else {
                showStateSnackbar(R.string.promiseDetail_delete_ask)
            }
        }
    }

    override fun onMapReady(map: NaverMap) {
        setObserver(map)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail_toolbar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item_detail_edit -> {
                val intent = Intent(this, PromiseSettingActivity::class.java).putExtra(
                    PROMISE_INFO_KEY,
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
        val promise = if (Build.VERSION.SDK_INT < 33) {
            intent.getParcelableExtra(PROMISE_INFO_KEY)
        } else {
            intent.getParcelableExtra(PROMISE_INFO_KEY, PromiseUiState::class.java)
        }

        if (promise != null) {
            promiseDetailViewModel.setPromiseInfo(promise)
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

    private fun setObserver(map: NaverMap) {
        promiseDetailViewModel.promiseInfo.observe(this) { promise ->
            promiseMemberAdapter.submitList(promise.members)

            val location = promise.destinationLocation
            val destinationLocation = convertLocation(location)

            moveCameraToDestination(destinationLocation, map)
            markDestinationOnMap(destinationLocation, map)
        }
    }

    private fun moveCameraToDestination(location: LatLng, map: NaverMap) {
        val cameraUpdate = CameraUpdate.scrollTo(location)
        map.moveCamera(cameraUpdate)
    }

    private fun markDestinationOnMap(location: LatLng, map: NaverMap) {
        marker.apply {
            position = location
            this.map = map
        }
    }

    private fun convertLocation(location: Location): LatLng {
        val tm128Location = Tm128(location.x.toDouble(), location.y.toDouble())
        return tm128Location.toLatLng()
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

    companion object {
        const val PROMISE_INFO_KEY = "promise"
        val marker = Marker()
    }

}