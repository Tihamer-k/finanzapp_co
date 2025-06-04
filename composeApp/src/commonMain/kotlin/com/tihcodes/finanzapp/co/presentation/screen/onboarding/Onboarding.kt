package com.tihcodes.finanzapp.co.presentation.screen.onboarding


import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigation.NavController
import com.tihcodes.finanzapp.co.domain.model.OnboardingPage
import com.tihcodes.finanzapp.co.getPlatform
import com.tihcodes.finanzapp.co.presentation.viewmodel.AuthViewModel
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.onboarding_1_piggy_bank_amico
import finanzapp_co.composeapp.generated.resources.onboarding_2_business_plan_amico
import finanzapp_co.composeapp.generated.resources.onboarding_3_investment_data_amico
import finanzapp_co.composeapp.generated.resources.onboarding_4_team_goals_amico
import finanzapp_co.composeapp.generated.resources.onboarding_description_1
import finanzapp_co.composeapp.generated.resources.onboarding_description_2_a
import finanzapp_co.composeapp.generated.resources.onboarding_description_2_b
import finanzapp_co.composeapp.generated.resources.onboarding_description_2_c
import finanzapp_co.composeapp.generated.resources.onboarding_description_3
import finanzapp_co.composeapp.generated.resources.onboarding_description_4
import finanzapp_co.composeapp.generated.resources.onboarding_title_1
import finanzapp_co.composeapp.generated.resources.onboarding_title_2
import finanzapp_co.composeapp.generated.resources.onboarding_title_3
import finanzapp_co.composeapp.generated.resources.onboarding_title_4
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun Onboarding(navController: NavController) {
    val userViewModel = koinViewModel<AuthViewModel>()
    val user = userViewModel.currentUser.collectAsState().value
    if (user?.name?.isNotEmpty() == true) {
        LaunchedEffect(user) {
            navController.navigate("home") {
                popUpTo("pre-login") { inclusive = true }
            }
        }
        return
    }
    val plt = getPlatform()
    val isWeb = plt.name == "IOS"
    val pages = listOf(
        OnboardingPage(
            imageRes = Res.drawable.onboarding_1_piggy_bank_amico,
            title = stringResource(Res.string.onboarding_title_1),
            description = stringResource(Res.string.onboarding_description_1)
        ),
        OnboardingPage(
            imageRes = Res.drawable.onboarding_2_business_plan_amico,
            title = stringResource(Res.string.onboarding_title_2),
            description =
                stringResource(Res.string.onboarding_description_2_a) + "\n" +
                        stringResource(Res.string.onboarding_description_2_b) + "\n" +
                        stringResource(Res.string.onboarding_description_2_c)
        ),
        OnboardingPage(
            imageRes = Res.drawable.onboarding_3_investment_data_amico,
            title = stringResource(Res.string.onboarding_title_3),
            description = stringResource(Res.string.onboarding_description_3)
        ),
        OnboardingPage(
            imageRes = Res.drawable.onboarding_4_team_goals_amico,
            title = stringResource(Res.string.onboarding_title_4),
            description = stringResource(Res.string.onboarding_description_4)
        )
    )
    val pagerState = rememberPagerState(pageCount = { pages.size })
    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState
    ) { page ->
        if (page < pages.size - 1) {
            OnboardingScreen(
                imageRes = pages[page].imageRes,
                title = pages[page].title,
                description = pages[page].description,
                onNextClick = {
                    if (page < pages.size) {
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(page + 1)
                        }
                    }
                },
                currentPage = page,
                total = pages.size,
            )
        } else {
            OnboardingScreen(
                imageRes = pages[page].imageRes,
                title = pages[page].title,
                description = pages[page].description,
                onNextClick = {
                    navController.navigate("pre-login") {
                        popUpTo("onboarding") {
                            inclusive = true
                        }
                    }
                },
                currentPage = page,
                total = pages.size,
            )
        }
    }
}

