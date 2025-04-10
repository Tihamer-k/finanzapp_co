package com.tihcodes.finanzapp.co.ui.screens.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.icon_finanzapp
import finanzapp_co.composeapp.generated.resources.pre_login_button_login
import finanzapp_co.composeapp.generated.resources.pre_login_button_sign_up
import finanzapp_co.composeapp.generated.resources.pre_login_link_forgot_password
import finanzapp_co.composeapp.generated.resources.pre_login_text
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun PreLoginScreen(navController: NavController) {

    Column (
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(0.6f))
        Image(
            painter = painterResource(Res.drawable.icon_finanzapp),
            contentDescription = "app logo",
            contentScale = ContentScale.Fit
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(Res.string.pre_login_text),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 28.dp)
            ,
        )
        Spacer(modifier = Modifier.weight(0.2f))
        Button(
            onClick = { navController.navigate("login") },
            modifier = Modifier.padding(horizontal = 16.dp)
                .size(width = 200.dp, height = 48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = stringResource(Res.string.pre_login_button_login),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary,
            )
        }
        Spacer(modifier = Modifier.height(10.dp))
        Button(
            onClick = { navController.navigate("register") },
            modifier = Modifier.padding(horizontal = 16.dp)
                .size(width = 200.dp, height = 48.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        ) {
            Text(
                text = stringResource(Res.string.pre_login_button_sign_up),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(modifier = Modifier.height(14.dp))
        Text(
            text = stringResource(Res.string.pre_login_link_forgot_password),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.inversePrimary,
            textDecoration = TextDecoration.Underline,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
                .clickable { navController.navigate("forgot-password") },
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}