package com.tihcodes.finanzapp.co.presentation.screen.rewards

import com.tihcodes.finanzapp.co.domain.model.Reward
import com.tihcodes.finanzapp.co.domain.model.RewardType

fun getRewardsContent(): List<Reward> {
    return listOf(
        Reward("medal_1", "Medalla de Inicio", "Completaste el primer curso", RewardType.MEDAL),
        Reward("medal_2", "Medalla de Ahorro", "Dominaste el ahorro", RewardType.MEDAL),
        Reward(
            "sim_1",
            "Simulador de Inversión",
            "Prueba escenarios reales",
            RewardType.SIMULATOR
        ),
        Reward(
            "sim_2",
            "Simulador de Finanzas",
            "Organiza tu futuro financiero",
            RewardType.SIMULATOR
        )
    )

}