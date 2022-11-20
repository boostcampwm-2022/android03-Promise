package com.boosters.promise.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boosters.promise.R
import com.boosters.promise.data.network.NetworkNotOnlineException
import com.boosters.promise.data.user.UserRepository
import com.boosters.promise.ui.signup.model.NameInputUiState
import com.boosters.promise.ui.signup.model.SignUpUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    val enterName = MutableLiveData<String>()

    private val _signUpUiState = MutableSharedFlow<SignUpUiState>()
    val signUpUiState = _signUpUiState.asSharedFlow()

    private val _nameInputUiState = MutableStateFlow(NameInputUiState())
    val nameInputUiState = _nameInputUiState.asStateFlow()

    fun requestSignUp() {
        val name = enterName.value ?: ""
        if (isCorrectName(name).not()) return

        viewModelScope.launch {
            _signUpUiState.emit(SignUpUiState.Loading)
            userRepository.requestSignUp(name).onSuccess {
                _signUpUiState.emit(SignUpUiState.Success)
            }.onFailure { throwable ->
                val messageResId = if (throwable is NetworkNotOnlineException) {
                    R.string.signUp_networkError
                } else {
                    R.string.signUp_signUpError
                }
                _signUpUiState.emit(SignUpUiState.Fail(messageResId))
            }
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

    companion object {
        private val nameValidationRegex = "[0-9a-zA-Z가-힣]{1,8}".toRegex()
    }

}