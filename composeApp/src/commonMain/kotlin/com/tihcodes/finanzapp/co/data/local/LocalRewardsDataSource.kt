package com.tihcodes.finanzapp.co.data.local

import com.finanzapp.Database
import com.tihcodes.finanzapp.co.domain.model.Reward
import com.tihcodes.finanzapp.co.domain.model.RewardType

class LocalRewardsDataSource(
    databaseDriverFactory: DatabaseDriverFactory
) {
    private val database = Database(
        driver = databaseDriverFactory.createDriver()
    )
    private val rewardsQueries = database.rewardQueries

    fun setReward(rewardsData: Reward) {
        try {
            rewardsQueries.insertReward(
                id = rewardsData.id,
                name = rewardsData.name,
                description = rewardsData.description,
                type = rewardsData.type.name,
                isUnlocked = if (rewardsData.isUnlocked) 1 else 0,
                userId = rewardsData.userId
            )
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: ${e.message}")
        }
    }

    fun getRewardsByUserId(idUser: String): List<Reward> {
        println("INFO: Reading rewards data from database")
        try {
            return rewardsQueries.getRewardsByUserId(idUser).executeAsList().map { reward ->
                Reward(
                    id = reward.id,
                    name = reward.name,
                    description = reward.description,
                    type = RewardType.valueOf(reward.type),
                    isUnlocked = reward.isUnlocked.toInt() == 1,
                    userId = reward.userId
                )
            }
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: ${e.message}")
            return emptyList()
        }
    }

    fun getRewardById(userId: String, id: String): Reward? {
        println("INFO: Reading reward data from database")
        try {
            return rewardsQueries.getRewardById(userId, id).executeAsOneOrNull()?.let { reward ->
                Reward(
                    id = reward.id,
                    name = reward.name,
                    description = reward.description,
                    type = RewardType.valueOf(reward.type),
                    isUnlocked = reward.isUnlocked.toInt() == 1,
                    userId = reward.userId
                )
            }
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: ${e.message}")
            return null
        }
    }
}
