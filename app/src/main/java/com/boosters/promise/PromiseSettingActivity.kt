package com.boosters.promise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import com.boosters.promise.databinding.ActivityPromiseSettingBinding
import com.google.android.material.chip.Chip
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PromiseSettingActivity : AppCompatActivity() {

    private var binding: ActivityPromiseSettingBinding? = null
    private val viewModel: PromiseSettingViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPromiseSettingBinding.inflate(layoutInflater)
        binding?.lifecycleOwner = this
        binding?.promiseSettingViewModel = viewModel
        setContentView(binding?.root)
        setObserver()
    }

    private fun setObserver() {
        viewModel.promiseMemberList.observe(this) {
            val chipGroup = binding?.chipGroupPromiseSetting
            val children = it?.mapIndexed { index, user ->
                val chip = LayoutInflater.from(chipGroup?.context)
                    .inflate(R.layout.item_promise_member, chipGroup, false) as Chip
                chip.isCheckable = false
                chip.text = user.userName
                chip.setOnCloseIconClickListener {
                    viewModel.removeMember(index)
                }
                chip
            }
            chipGroup?.removeAllViews()
            children?.forEach { chip ->
                chipGroup?.addView(chip)
            }
        }
    }

}