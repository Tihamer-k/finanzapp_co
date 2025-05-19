package com.tihcodes.finanzapp.co.data.repository

import com.tihcodes.finanzapp.co.data.local.LocalCourseTrackingDataSource
import com.tihcodes.finanzapp.co.data.remote.FirebaseCourseTrackingDataSource
import com.tihcodes.finanzapp.co.domain.model.CourseTrackingData
import com.tihcodes.finanzapp.co.domain.repository.CourseTrackingRepository

class CourseTrackingRepositoryImpl(
    private val localDataSource: LocalCourseTrackingDataSource,
    private val remoteDataSource: FirebaseCourseTrackingDataSource
) : CourseTrackingRepository {
    override suspend fun getAllCourseTracking(): List<CourseTrackingData> {
        val localData = localDataSource.getAllCourseTracking()
        if (localData.isNotEmpty()) return localData

        val remoteData = remoteDataSource.getAllCourseTrackingData()
        if (remoteData.isNotEmpty()) return remoteData

        return emptyList()
    }

    override suspend fun getCourseTrackingByUserId(idUser: String): List<CourseTrackingData> {
        val localData = localDataSource.getCourseTrackingByUserId(idUser)
        if (localData.isNotEmpty()) return localData
        val remoteData = remoteDataSource.getCourseTrackingDataByUserId(idUser)
        if (remoteData.isNotEmpty()) {
            for (remoteItem in remoteData) localDataSource.setCourseTrackingData(remoteItem)
            return remoteData
        }
        return emptyList()
    }

    override suspend fun getCourseTrackingById(idUser: String, courseId: String): CourseTrackingData? {
        val localData = localDataSource.getCourseTrackingById(idUser, courseId)
        if (localData != null) return localData
        val remoteData = remoteDataSource.getCourseTrackingDataById(idUser, courseId)
        if (remoteData != null) {
            localDataSource.setCourseTrackingData(remoteData)
            return remoteData
        }
        return null
    }

    override suspend fun setCourseTracking(courseTracking: CourseTrackingData) {
        localDataSource.setCourseTrackingData(courseTracking)
        remoteDataSource.setCourseTrackingData(courseTracking)
    }
}