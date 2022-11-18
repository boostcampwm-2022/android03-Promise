package com.boosters.promise.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.R
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.ui.signup.model.NameInputUiState
import com.boosters.promise.ui.signup.model.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val enterName = MutableLiveData<String>()

    private val _signUpUiState = MutableStateFlow(SignUpUiState())
    val signUpUiState = _signUpUiState.asStateFlow()

    private val _nameInputUiState = MutableStateFlow(NameInputUiState())
    val nameInputUiState = _nameInputUiState.asStateFlow()

    fun requestSignUp() {
        val name = enterName.value ?: ""
        if (name.matches(nameValidationRegex).not()) {
            _nameInputUiState.value = NameInputUiState(
                isNameValidationFail = true,
                nameValidationErrorTextResId = R.string.signUp_inputError
            )
            return
        }
        _nameInputUiState.value = NameInputUiState()

        _signUpUiState.update { SignUpUiState(isRegistering = true) }
        viewModelScope.launch {
            userRepository.requestSignUp(name).onSuccess {
                _signUpUiState.update { SignUpUiState(isCompleteSignUp = true) }
            }.onFailure {
                _signUpUiState.update {
                    SignUpUiState(
                        isErrorSignUp = false,
                        signUpErrorMessageResId = R.string.signUp_signUpError
                    )
                }
            }
        }
    }

    companion object {
        private val nameValidationRegex = "[0-9a-zA-Z가-힣]{1,8}".toRegex()
    }

}