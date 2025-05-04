package com.tihcodes.finanzapp.co.presentation.screen.records

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.tihcodes.finanzapp.co.data.local.CategoryDatabase
import com.tihcodes.finanzapp.co.domain.model.TransactionItem
import com.tihcodes.finanzapp.co.domain.model.TransactionType
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.ic_food
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.koinInject
import kotlin.math.absoluteValue

@Composable
fun TransactionItemCard(transaction: TransactionItem) {
    val categoryBD = koinInject<CategoryDatabase>()
    val viewModel = koinInject<AuthViewModel>()

    val user = viewModel.currentUser.value
    val userId = user?.id ?: ""

    Surface(
        shape = RoundedCornerShape(16.dp),
        tonalElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon = getIconForCategory(transaction.category, userId, categoryBD)
            Image(
                painter = painterResource(icon),
                contentDescription = transaction.title,
                modifier = Modifier.size(48.dp)
                    .padding(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    .padding(8.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = transaction.title,
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${transaction.category} - ${transaction.date.dayOfMonth} ${
                        transaction.date.month.name.take(
                            3
                        )
                    }",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Text(
                text = if (transaction.type == TransactionType.INCOME) "+$${transaction.amount}" else "-$${transaction.amount.absoluteValue}",
                style = MaterialTheme.typography.bodyMedium,
                color = if (transaction.type == TransactionType.INCOME) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }
    }
}

fun getIconForCategory(category: String, userId: String, categoryBD: CategoryDatabase): DrawableResource {
    return categoryBD.getCategoryByNameAndUserId(category, userId)?.icon ?: Res.drawable.ic_food
}
