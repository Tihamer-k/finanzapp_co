package com.tihcodes.finanzapp.co.data.local

import com.finanzapp.Database
import com.tihcodes.finanzapp.co.domain.model.CourseTrackingData

class LocalCourseTrackingDataSource(
    databaseDriverFactory: DatabaseDriverFactory
) {
    private val database = Database(
        driver = databaseDriverFactory.createDriver()
    )
    private val queries = database.coursetrackingQueries

    fun getCourseTrackingById(idUser: String, courseId: String): CourseTrackingData? {
        return try {
            queries.getCourseTrackingById(courseId, idUser).executeAsOneOrNull().let {
                if (it != null) {
                    CourseTrackingData(
                        id = it.id,
                        title = it.title,
                        isCompleted = it.isCompleted,
                        isUnlocked = it.isUnlocked,
                        userId = it.userId,
                        rewardId = it.rewardId,
                        hasPendingQuestions = it.hasPendingQuestions
                    )
                } else {
                    null
                }
            }
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: getting course tracking by ID: ${e.message}")
            null
        }
    }

    fun getCourseTrackingByUserId(idUser: String): List<CourseTrackingData> {
        return try {
            queries.getCourseTrackingByUserId(idUser).executeAsList().map {
                CourseTrackingData(
                    id = it.id,
                    title = it.title,
                    isCompleted = it.isCompleted,
                    isUnlocked = it.isUnlocked,
                    userId = it.userId,
                    rewardId = it.rewardId,
                    hasPendingQuestions = it.hasPendingQuestions
                )
            }
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: getting course tracking by user ID: ${e.message}")
            emptyList()
        }
    }

    fun getAllCourseTracking(): List<CourseTrackingData> {
        return try {
            queries.getAllCourseTrackings().executeAsList().map {
                CourseTrackingData(
                    id = it.id,
                    title = it.title,
                    isCompleted = it.isCompleted,
                    isUnlocked = it.isUnlocked,
                    userId = it.userId,
                    rewardId = it.rewardId,
                    hasPendingQuestions = it.hasPendingQuestions
                )
            }
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: getting all course tracking: ${e.message}")
            emptyList()
        }
    }

    fun setCourseTrackingData(courseTrackingData: CourseTrackingData) {
        try {
            queries.insertCourseTracking(
                id = courseTrackingData.id,
                title = courseTrackingData.title,
                isCompleted = courseTrackingData.isCompleted,
                isUnlocked = courseTrackingData.isUnlocked,
                userId = courseTrackingData.userId,
                rewardId = courseTrackingData.rewardId ?: "",
                hasPendingQuestions = courseTrackingData.hasPendingQuestions
            )
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: setting course tracking data: ${e.message}")

        }
    }


}