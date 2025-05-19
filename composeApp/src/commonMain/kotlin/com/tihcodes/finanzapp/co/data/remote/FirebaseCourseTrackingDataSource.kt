package com.tihcodes.finanzapp.co.data.remote

import com.tihcodes.finanzapp.co.domain.model.CourseTrackingData
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.firestore

class FirebaseCourseTrackingDataSource {
    private val firestore = Firebase.firestore

    suspend fun getCourseTrackingDataById(userId: String, courseId: String): CourseTrackingData? {
        return try {
            val document = firestore.collection("courseTracking")
                .document(userId)
                .collection("courses")
                .document(courseId)
                .get()
            document.data<CourseTrackingData>()
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: getting course tracking data by ID: ${e.message}")
            null
        }
    }

    suspend fun getCourseTrackingDataByUserId(userId: String): List<CourseTrackingData> {
        return try {
            val documents = firestore.collection("courseTracking")
                .document(userId)
                .collection("courses")
                .get()
            documents.documents.map { it.data<CourseTrackingData>() }
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: getting course tracking data by UserId: ${e.message}")
            emptyList()
        }
    }

    suspend fun getAllCourseTrackingData(): List<CourseTrackingData> {
        return try {
            val documents = firestore.collection("courseTracking").get()
            documents.documents.map { it.data<CourseTrackingData>() }
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: getting all course tracking data: ${e.message}")
            emptyList()
        }
    }

    suspend fun setCourseTrackingData(courseTrackingData: CourseTrackingData) {
        try {
            val courseTrackingValue = mapOf(
                "id" to courseTrackingData.id,
                "title" to courseTrackingData.title,
                "isCompleted" to courseTrackingData.isCompleted,
                "isUnlocked" to courseTrackingData.isUnlocked,
                "userId" to courseTrackingData.userId,
            )
            firestore.collection("courseTracking")
                .document(courseTrackingData.userId)
                .collection("courses")
                .document(courseTrackingData.id)
                .set(courseTrackingValue)
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: setting course tracking data: ${e.message}")
        }
    }
}