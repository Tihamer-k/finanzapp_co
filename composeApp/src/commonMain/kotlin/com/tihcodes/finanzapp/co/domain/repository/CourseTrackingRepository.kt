package com.tihcodes.finanzapp.co.domain.repository

import com.tihcodes.finanzapp.co.domain.model.CourseTrackingData


interface CourseTrackingRepository {
    suspend fun getAllCourseTracking(): List<CourseTrackingData>
    suspend fun getCourseTrackingByUserId(idUser: String): List<CourseTrackingData>
    suspend fun getCourseTrackingById(idUser: String, courseId: String): CourseTrackingData?
    suspend fun setCourseTracking(courseTracking: CourseTrackingData)
}