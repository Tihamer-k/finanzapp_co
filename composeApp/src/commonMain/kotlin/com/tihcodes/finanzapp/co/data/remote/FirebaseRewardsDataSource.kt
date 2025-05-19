package com.tihcodes.finanzapp.co.data.remote

import com.tihcodes.finanzapp.co.domain.model.Reward
import com.tihcodes.finanzapp.co.domain.model.RewardType
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.DocumentSnapshot
import dev.gitlive.firebase.firestore.firestore

class FirebaseRewardsDataSource {
    private val firestore = Firebase.firestore

    suspend fun getRewardById(userId: String, rewardId: String): Reward? {
        return try {
            val document = firestore.collection("rewards")
                .document(userId)
                .collection("userRewards")
                .document(rewardId)
                .get()
            document.toReward()
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: getting reward by ID: ${e.message}")
            null
        }
    }

    suspend fun getRewardsByUserId(userId: String): List<Reward> {
        return try {
            val documents = firestore.collection("rewards")
                .document(userId)
                .collection("userRewards")
                .get()
            documents.documents.mapNotNull { it.toReward() }
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: getting rewards by UserId: ${e.message}")
            emptyList()
        }
    }

    suspend fun setReward(reward: Reward) {
        try {
            val rewardValue = mapOf(
                "id" to reward.id,
                "name" to reward.name,
                "description" to reward.description,
                "type" to reward.type.name,
                "isUnlocked" to reward.isUnlocked,
                "userId" to reward.userId
            )
            firestore.collection("rewards")
                .document(reward.userId)
                .collection("userRewards")
                .document(reward.id)
                .set(rewardValue)
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: setting reward: ${e.message}")
        }
    }

    private fun DocumentSnapshot.toReward(): Reward? {
        return try {
            Reward(
                id = get("id") as String,
                name = get("name") as String,
                description = get("description") as String,
                type = RewardType.valueOf(get("type") as String),
                isUnlocked = get("isUnlocked") as Boolean,
                userId = get("userId") as String
            )
        } catch (e: Exception) {
            println("Error ${this::class.simpleName}: parsing reward: ${e.message}")
            null
        }
    }
}
