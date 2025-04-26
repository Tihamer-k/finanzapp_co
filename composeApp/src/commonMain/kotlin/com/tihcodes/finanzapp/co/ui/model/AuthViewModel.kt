package com.tihcodes.finanzapp.co.ui.model

import androidx.lifecycle.viewModelScope
import com.tihcodes.finanzapp.co.data.Course
import com.tihcodes.finanzapp.co.data.User
import com.tihcodes.finanzapp.co.service.AuthService
import com.tihcodes.finanzapp.co.ui.common.BaseViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class AuthViewModel(
    private val authService: AuthService,
) : BaseViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState = _uiState.asStateFlow()

    private val _emailError = MutableStateFlow(false)
    val emailError = _emailError.asStateFlow()

    private val _passwordError = MutableStateFlow(false)
    val passwordError = _passwordError.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser = _currentUser.asStateFlow()

    private val _isProcessing = MutableStateFlow(false)
    val isProcessing = _isProcessing.asStateFlow()

    private val _isButtonEnabled = MutableStateFlow(false)
    val isButtonEnabled = _isButtonEnabled.asStateFlow()

    private val _authState = MutableStateFlow(false)
    val authState = _authState.asStateFlow()

    private val _isSignIn = MutableStateFlow(false)
    val isSignIn = _isSignIn.asStateFlow()
    init {
        viewModelScope.launch {
            uiState.collect { state ->
                _isButtonEnabled.value = state.email.isNotBlank()
                        && state.password.isNotBlank()
            }
        }
    }


    init {
        launchWithCatchingException {
            authService.currentUser.collect {
                _currentUser.value = it
            }
        }
    }

    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses

    init {
        _courses.value = listOf(
            Course(id = "1", title = "Introducción a Finanzas", description = "Conceptos básicos", isUnlocked = true),
            Course(id = "2", title = "Ahorro Inteligente", description = "Cómo ahorrar mejor"),
            Course(id = "3", title = "Inversiones Básicas", description = "Fundamentos de inversión"),
            Course(id = "4", title = "Planificación Financiera", description = "Organiza tu futuro")
        )
    }

    fun completeCourse(courseId: String) {
        val updatedList = _courses.value.mapIndexed { index, course ->
            when {
                course.id == courseId -> course.copy(isCompleted = true)
                course.id != courseId && index == _courses.value.indexOfFirst { it.id == courseId } + 1 ->
                    course.copy(isUnlocked = true)
                else -> course
            }
        }
        _courses.value = updatedList
    }

    val progress: StateFlow<Float> = _courses.map { list ->
        val total = list.size
        val completed = list.count { it.isCompleted }
        if (total == 0) 0f else completed.toFloat() / total
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = 0f
    )


    fun onEmailChange(newValue: String) {
        _uiState.update { it.copy(email = newValue) }
        //reset error
        if (newValue.isNotBlank()) _emailError.value = false
        // validate email format
        if (!newValue.contains("@") || !newValue.contains(".")) {
            _emailError.value = true
        }
    }

    fun onPasswordChange(newValue: String) {
        _uiState.update { it.copy(password = newValue) }
        //reset error
        if (newValue.isNotBlank()) _passwordError.value = false
        // validate password length
        if (newValue.length < 8) {
            _passwordError.value = true
        }
    }

    fun onNameChange(newValue: String) {
        _uiState.update { it.copy(name = newValue) }

    }

    fun onSurnameChange(newValue: String) {
        _uiState.update { it.copy(surname = newValue) }
    }

    fun onPhoneChange(newValue: String) {
        _uiState.update { it.copy(phone = newValue) }
    }

    fun onDateChange(newValue: String) {
        _uiState.update { it.copy(date = newValue) }
    }

     fun onSignInClick() {

        launchWithCatchingException {
            _isProcessing.value = true
            try {
                authService.authenticate(_uiState.value.email, _uiState.value.password)
                _authState.value = true
                _isSignIn.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _authState.value = false
                _isSignIn.value = false
            }
            _isProcessing.value = false
        }
     }


    fun onSignOut() {
        launchWithCatchingException {
            authService.signOut()
            _isSignIn.value = false
        }
    }

    fun onRegister() {
        launchWithCatchingException {
            _isProcessing.value = true
            try {
                authService.createUserFStore(
                    name = _uiState.value.name,
                    surname = _uiState.value.surname,
                    email = _uiState.value.email,
                    phone = _uiState.value.phone,
                    password = _uiState.value.password,
                    date = _uiState.value.date
                )
                _authState.value = true
                _isSignIn.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _authState.value = false
                _isSignIn.value = false
            }
            _isProcessing.value = false
        }
    }

    fun onReset() {
        launchWithCatchingException {
            _isProcessing.value = true
            try {
                // Check if the email is valid before sending the reset password email
                if (!authService.isExistingUser(uiState.value.email)) {
                    authService.resetPassword(_uiState.value.email)
                    _authState.value = true
                } else {
                    _authState.value = false
                    _isProcessing.value = false
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _authState.value = false
            }
            _isProcessing.value = false
        }
    }

    fun syncUserData() {
        viewModelScope.launch {
            _isProcessing.value = true
            try {
                val userId = authService.currentUserId
                val userData = authService.getUserData(userId)
                _currentUser.value = userData
                _uiState.update {
                    it.copy(
                        name = userData.name,
                        surname = userData.surname,
                        email = userData.email,
                        phone = userData.phone,
                        date = userData.date
                    )
                }
                _authState.value = true
            } catch (e: Exception) {
                e.printStackTrace()
                _authState.value = false
            }
            _isProcessing.value = false
        }
    }
}


data class LoginUiState(
    var email: String = "",
    var password: String = "",
    var name: String = "",
    var surname: String = "",
    var phone: String = "",
    var date: String = "",
)