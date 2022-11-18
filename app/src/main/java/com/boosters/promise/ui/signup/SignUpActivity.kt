package com.boosters.promise.ui.signup

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivitySignUpBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private val signUpViewModel: SignUpViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivitySignUpBinding?>(this, R.layout.activity_sign_up).apply {
            lifecycleOwner = this@SignUpActivity
            signUpViewModel = this@SignUpActivity.signUpViewModel
        }

        binding.buttonSignUpSignUpRequest.setOnClickListener {
            binding.editTextSignUpName.clearFocus()
            getSystemService(Context.INPUT_METHOD_SERVICE).run {
                if (this is InputMethodManager) hideSoftInputFromWindow(binding.editTextSignUpName.windowToken, 0)
            }
            signUpViewModel.requestSignUp()
        }

        signUpViewModel.signUpUiState.observe(this) { signUpUiState ->
            if (signUpUiState.isCompleteSignUp) {
//                startActivity(Intent(this, PromiseSettingActivity::class.java))
                // TODO: Home 약속 리스트 화면으로 이동 구현
                finish()
            } else {
                signUpUiState.signUpErrorMessageResId?.let {
                    Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

}