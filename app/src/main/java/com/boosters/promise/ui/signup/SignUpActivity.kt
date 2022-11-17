package com.boosters.promise.ui.signup

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import com.boosters.promise.R
import com.boosters.promise.databinding.ActivitySignUpBinding
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
    }

}