package com.boosters.promise.ui.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.R
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.ui.signup.model.NameInputUiState
import com.boosters.promise.ui.signup.model.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val enterName = MutableLiveData<String>()

    private val _signUpUiState = MutableLiveData<SignUpUiState>()
    val signUpUiState: LiveData<SignUpUiState> = _signUpUiState

    private val _isCompleteSignUp = MutableLiveData(false)
    val isCompleteSignUp: LiveData<Boolean> = _isCompleteSignUp

    private val _nameInputUiState = MutableLiveData<NameInputUiState>()
    val nameInputUiState: LiveData<NameInputUiState> = _nameInputUiState

    fun requestSignUp() {
        enterName.value?.let { name ->
            if (nameValidationRegex.matches(name).not()) {
                _nameInputUiState.value = NameInputUiState(
                    isNameValidationFail = true,
                    nameValidationErrorTextResId = R.string.signUp_inputError
                )
                return
            } else {
                _nameInputUiState.value = NameInputUiState()
            }
            _signUpUiState.value = SignUpUiState(true)
            viewModelScope.launch {
                userRepository.requestSignUp(name)
                _signUpUiState.value = SignUpUiState()
                _isCompleteSignUp.value = true
            }
        }
    }

    companion object {
        private val nameValidationRegex = "[0-9a-zA-Z가-힣]{1,8}".toRegex()
    }
}