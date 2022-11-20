package com.boosters.promise.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.R
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.ui.signup.model.NameInputUiState
import com.boosters.promise.ui.signup.model.SignUpUiState
import com.boosters.promise.util.NetworkConnectUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val networkConnectUtil: NetworkConnectUtil
) : ViewModel() {

    val enterName = MutableLiveData<String>()

    private val _signUpUiState = MutableStateFlow<SignUpUiState>(SignUpUiState.Nothing)
    val signUpUiState = _signUpUiState.asStateFlow()

    private val _nameInputUiState = MutableStateFlow(NameInputUiState())
    val nameInputUiState = _nameInputUiState.asStateFlow()

    fun requestSignUp() {
        val name = enterName.value ?: ""
        if (isCorrectName(name).not() || isOnline().not()) return

        _signUpUiState.update { SignUpUiState.Loading }
        viewModelScope.launch {
            userRepository.requestSignUp(name).onSuccess {
                _signUpUiState.update { SignUpUiState.Success }
            }.onFailure {
                _signUpUiState.update {
                    SignUpUiState.Fail(R.string.signUp_signUpError)
                }
            }
            _signUpUiState.update { SignUpUiState.Nothing }
        }
    }

    private fun isCorrectName(name: String): Boolean {
        return name.matches(nameValidationRegex).also { isCorrectName ->
            _nameInputUiState.value = if (isCorrectName.not()) {
                NameInputUiState(
                    isNameValidationFail = true,
                    nameValidationErrorTextResId = R.string.signUp_inputError
                )
            } else {
                NameInputUiState()
            }
        }
    }

    private fun isOnline(): Boolean {
        return networkConnectUtil.isOnline().also { isOnline ->
            if (isOnline.not()) _signUpUiState.update { SignUpUiState.Fail(R.string.signUp_networkError) }
        }
    }

    companion object {
        private val nameValidationRegex = "[0-9a-zA-Z가-힣]{1,8}".toRegex()
    }

}