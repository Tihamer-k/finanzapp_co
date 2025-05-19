package com.tihcodes.finanzapp.co.domain.repository

import com.tihcodes.finanzapp.co.domain.model.Reward

interface RewardsRepository {
    suspend fun getRewardById(userId: String, id: String): Reward?
    suspend fun getRewardsByUserId(idUser: String): List<Reward>
    suspend fun setReward(reward: Reward)
}