package com.tihcodes.finanzapp.co.presentation.screen.rewards

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tihcodes.finanzapp.co.domain.model.Reward
import com.tihcodes.finanzapp.co.domain.model.RewardType
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.rewards__gold_medal
import finanzapp_co.composeapp.generated.resources.rewards__medal_cup
import org.jetbrains.compose.resources.painterResource

@Composable
fun RewardItem(
    reward: Reward,
    onSimulatorClick: (Reward) -> Unit = {}
) {
    val background = if (reward.isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant
    val textAlpha = if (reward.isUnlocked) 1f else 0.4f
    val isClickable = reward.isUnlocked && reward.type == RewardType.SIMULATOR


    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        color = background,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isClickable) {
                if (isClickable) onSimulatorClick(reward)
            }
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = when (reward.type) {
                RewardType.MEDAL -> Res.drawable.rewards__gold_medal//ic_medal
                RewardType.SIMULATOR -> Res.drawable.rewards__medal_cup//ic_simulator
            }

            Image(
                painter = painterResource(icon),
                contentDescription = reward.name,
                modifier = Modifier.size(40.dp),
                alpha = textAlpha
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = reward.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = textAlpha)
                )
                Text(
                    text = reward.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = textAlpha)
                )
            }
        }
    }
}
