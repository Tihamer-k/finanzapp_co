package com.tihcodes.finanzapp.co.data.repository

import com.tihcodes.finanzapp.co.data.local.LocalRewardsDataSource
import com.tihcodes.finanzapp.co.data.remote.FirebaseRewardsDataSource
import com.tihcodes.finanzapp.co.domain.model.Reward
import com.tihcodes.finanzapp.co.domain.repository.RewardsRepository

class RewardsRepositoryImpl(
    private val localDataSource: LocalRewardsDataSource,
    private val remoteDataSource: FirebaseRewardsDataSource,
): RewardsRepository {
    override suspend fun getRewardById(userId: String, id: String): Reward? {
        val localData = localDataSource.getRewardById(userId, id)
        if (localData != null) return localData
        val remoteData = remoteDataSource.getRewardById(
            userId = userId,
            rewardId = id
        )
        if (remoteData != null) {
            localDataSource.setReward(remoteData)
            return remoteData
        }
        return null
    }

    override suspend fun getRewardsByUserId(idUser: String): List<Reward> {
        val localData = localDataSource.getRewardsByUserId(idUser)
        if (localData.isNotEmpty()) return localData
        val remoteData = remoteDataSource.getRewardsByUserId(idUser)
        if (remoteData.isNotEmpty()) {
            for (remoteItem in remoteData) localDataSource.setReward(remoteItem)
            return remoteData
        }
        return emptyList()
    }

    override suspend fun setReward(reward: Reward) {
        localDataSource.setReward(reward)
        remoteDataSource.setReward(reward)
    }

}