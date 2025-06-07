package com.tihcodes.finanzapp.co.presentation.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.viewModelScope
import com.tihcodes.finanzapp.co.domain.model.Course
import com.tihcodes.finanzapp.co.domain.model.CourseTrackingData
import com.tihcodes.finanzapp.co.domain.model.Reward
import com.tihcodes.finanzapp.co.domain.repository.CourseTrackingRepository
import com.tihcodes.finanzapp.co.domain.repository.RewardsRepository
import com.tihcodes.finanzapp.co.presentation.common.BaseViewModel
import com.tihcodes.finanzapp.co.presentation.screen.learn.getCourses
import com.tihcodes.finanzapp.co.presentation.screen.rewards.getRewardsContent
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class CourseTrackingViewModel(
    private val courseTrackingRepository: CourseTrackingRepository,
    private val rewardRepository: RewardsRepository,
) : BaseViewModel() {
    private val _courseTracking = mutableStateOf<List<CourseTrackingData>>(emptyList())
    private val _courseTrackingById = mutableStateOf<CourseTrackingData?>(null)
    val courseTrackingById: State<CourseTrackingData?> = _courseTrackingById
    private val _courseTrackingByUserId = mutableStateOf<List<CourseTrackingData>>(emptyList())
    private val _courses = MutableStateFlow<List<Course>>(emptyList())
    val courses: StateFlow<List<Course>> = _courses.asStateFlow()
    private val _rewards = MutableStateFlow<List<Reward>>(emptyList())
    val rewards: StateFlow<List<Reward>> = _rewards.asStateFlow()
    private val _idUser = MutableStateFlow("")
    private val _rewardById = mutableStateOf<Reward?>(null)


    init {
        if (_courses.value.isEmpty()) {
            _courses.value = getCourses()
            _rewards.value = getRewardsContent()
        } else {
            loadCourses(_idUser.value)
            loadRewards(_idUser.value)
        }
    }

    fun setUserId(userId: String) {
        _idUser.value = userId
        loadCourses(userId)
        println("INFO: User $userId was set in CourseTrackingViewModel")
    }

    private fun getAllCourseTracking() {
        viewModelScope.launch {
            _courseTracking.value = courseTrackingRepository.getAllCourseTracking()
        }
    }

    private fun setCourseTracking(courseTracking: CourseTrackingData) {
        viewModelScope.launch {
            courseTrackingRepository.setCourseTracking(courseTracking)
            getCourseTrackingByUserId(courseTracking.userId)
        }
    }

    private fun getCourseTrackingByUserId(idUser: String) {
        viewModelScope.launch {
            _courseTrackingByUserId.value =
                courseTrackingRepository.getCourseTrackingByUserId(idUser)
        }
    }

    private fun getRewardById(idUser: String, idReward: String) {
        viewModelScope.launch {
            _rewardById.value = rewardRepository.getRewardById(idUser, idReward)

        }
    }

    private fun getRewardsByUserId(idUser: String) {
        viewModelScope.launch {
            _rewards.value = rewardRepository.getRewardsByUserId(idUser)
        }
    }

    private fun setReward(reward: Reward) {
        viewModelScope.launch {
            rewardRepository.setReward(reward)
            getRewardsByUserId(idUser = reward.userId)
        }
    }

    fun loadCourses(userId: String) {
        viewModelScope.launch {
            val userCourseTracking = courseTrackingRepository.getCourseTrackingByUserId(userId)
            val allCourses = getCourses()
            var unlockNext = false
            // Sincroniza los datos de los cursos con los datos de seguimiento del usuario
            _courses.value = allCourses.mapIndexed { index, course ->
                val courseTracking = userCourseTracking.find { it.id == course.id }
                val isCompleted = courseTracking?.isCompleted == 1L
                val isUnlocked = when {
                    courseTracking != null -> courseTracking.isUnlocked == 1L
                    index == 0 -> true // El primer curso siempre está desbloqueado
                    else -> unlockNext
                }
                val hasPendingQuestions = !isCompleted
                val result = course.copy(
                    isCompleted = isCompleted,
                    isUnlocked = isUnlocked,
                    rewardId = courseTracking?.rewardId,
                    hasPendingQuestions = hasPendingQuestions
                )
                unlockNext = isCompleted
                result
            }
        }
    }

    fun loadRewards(userId: String) {
        viewModelScope.launch {
            val userRewards = rewardRepository.getRewardsByUserId(userId)
            val allRewards = getRewardsContent()
            _rewards.value = allRewards.map { reward ->
                val userReward = userRewards.find { it.id == reward.id }
                reward.copy(
                    isUnlocked = userReward?.isUnlocked ?: false, // Mantener bloqueados si no están desbloqueados
                    userId = userId
                )
            }
        }
    }

    val progress: StateFlow<Float> = _courses.map { list ->
        val total = list.size
        val completed = list.count { it.isCompleted }
        if (total == 0) 0f else completed.toFloat() / total
    }.stateIn(viewModelScope, SharingStarted.Eagerly, 0f)

    fun completeCourse(courseId: String, userId: String, isCompleted: Boolean) {
        val updatedCourses = _courses.value.mapIndexed { index, course ->
            when {
                course.id == courseId -> course.copy(
                    isCompleted = isCompleted,
                    hasPendingQuestions = false,
                )

                course.id != courseId && index == _courses.value.indexOfFirst { it.id == courseId } + 1 ->
                    course.copy(isUnlocked = true) // Desbloquea el siguiente curso
                else -> course
            }
        }
        val updatedRewards = _rewards.value.map { reward ->
            when {
                reward.id == courseId -> reward.copy(isUnlocked = true)
                else -> reward
            }
        }

        // Actualiza el estado de CourseTracking en la base de datos
        val completedCourse = updatedCourses.find { it.id == courseId }
        if (completedCourse != null) {
            val courseTracking = CourseTrackingData(
                id = completedCourse.id,
                title = completedCourse.title,
                isCompleted = if (completedCourse.isCompleted) 1L else 0L,
                isUnlocked = if (completedCourse.isUnlocked) 1L else 0L,
                userId = userId,
                rewardId = completedCourse.rewardId,
                hasPendingQuestions = if (completedCourse.hasPendingQuestions) 1L else 0L
            )
            setCourseTracking(courseTracking)
        }

        val unlockedRewardId = completedCourse?.rewardId
        // Actualiza el estado de Reward en la base de datos
        if (unlockedRewardId != null) {
            val reward = updatedRewards.find { it.id == unlockedRewardId }
            if (reward != null) {
                setReward(
                    reward.copy(
                        isUnlocked = true,
                        userId = userId
                    )
                )
            }
        }
        _courses.value = updatedCourses
        _rewards.value = updatedRewards
    }

}
