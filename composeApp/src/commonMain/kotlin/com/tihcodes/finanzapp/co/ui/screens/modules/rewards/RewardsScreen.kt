package com.tihcodes.finanzapp.co.ui.screens.modules.rewards

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.ui.BottomNavBar
import com.tihcodes.finanzapp.co.ui.TopNavBar
import com.tihcodes.finanzapp.co.ui.model.AuthViewModel

@Composable
fun RewardsScreen(
    viewModel: AuthViewModel,
    onLogoutClick: () -> Unit,
    navController: NavHostController
) {
    val rewards by viewModel.rewards.collectAsState()


    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = "Recompensas",
                notificationsCount = 0,
                showBackButton = false,
            )
        },
        bottomBar = {
            BottomNavBar(
                indexIn = 4,
                onItemClick = navController
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(paddingValues)

        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
                    .clip(RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp))
                    .background(MaterialTheme.colorScheme.background)
                    .padding(16.dp)
            ) {

                Text(
                    text = "Tus Recompensas",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(rewards) { reward ->
                        RewardItem(
                            reward = reward,
                            onSimulatorClick = {
                                navController.navigate("simulator/${it.name}")
                            }
                        )
                    }
                }
            }
        }

    }
}
