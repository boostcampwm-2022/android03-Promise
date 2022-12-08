package com.boosters.promise.ui.signup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivitySignUpBinding
import com.boosters.promise.ui.loading.LoadingDialog
import com.boosters.promise.ui.promisecalendar.PromiseCalendarActivity
import com.boosters.promise.ui.signup.model.SignUpUiState
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val signUpViewModel: SignUpViewModel by viewModels()

    private lateinit var loadingDialog: LoadingDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView<ActivitySignUpBinding?>(this, R.layout.activity_sign_up)
                .apply {
                    lifecycleOwner = this@SignUpActivity
                }

        loadingDialog = LoadingDialog(this, getString(R.string.dialogLoading_signUp_message))

        initSignUpButton()
        bindVariable()
    }

    private fun initSignUpButton() {
        binding.buttonSignUpSignUpRequest.setOnClickListener {
            binding.editTextSignUpName.clearFocus()
            getSystemService(Context.INPUT_METHOD_SERVICE).run {
                if (this is InputMethodManager) hideSoftInputFromWindow(
                    binding.editTextSignUpName.windowToken,
                    0
                )
            }
            signUpViewModel.requestSignUp()
        }
    }

    private fun bindVariable() {
        binding.enterName = this@SignUpActivity.signUpViewModel.enterName

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                signUpViewModel.nameInputUiState.collectLatest {
                    binding.nameInputUiState = it
                }
            }
        }

        lifecycleScope.launch {
            signUpViewModel.signUpUiState.collect { signUpUiState ->
                when (signUpUiState) {
                    is SignUpUiState.Loading -> loadingDialog.show()
                    is SignUpUiState.Success -> {
                        loadingDialog.dismiss()
                        startActivity(
                            Intent(
                                this@SignUpActivity,
                                PromiseCalendarActivity::class.java
                            )
                        ).also { finish() }
                    }
                    is SignUpUiState.Fail -> {
                        loadingDialog.dismiss()
                        signUpUiState.signUpErrorMessageResId?.let {
                            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }

}