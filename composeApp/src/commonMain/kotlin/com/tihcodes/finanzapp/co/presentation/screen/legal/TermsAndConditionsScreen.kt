package com.tihcodes.finanzapp.co.presentation.screen.legal

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.tihcodes.finanzapp.co.presentation.components.TopNavBar
import finanzapp_co.composeapp.generated.resources.Res
import finanzapp_co.composeapp.generated.resources.agreement_amico
import org.jetbrains.compose.resources.painterResource

@Composable
fun TermsAndConditionsScreen(
    navController: NavHostController,
) {
    Scaffold(
        topBar = {
            TopNavBar(
                navController = navController,
                title = "Términos y condiciones",
                notificationsCount = 0,
                showBackButton = false,
            )
        },
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
                    .verticalScroll(rememberScrollState())
            ) {
                Spacer(modifier = Modifier.weight(0.2f))

                Image(
                    painter = painterResource(Res.drawable.agreement_amico),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(180.dp)
                )

                Column(
                    modifier = Modifier.padding(horizontal = 12.dp)
                ) {
                    Text(
                        text = "Última actualización: Mayo 2025\n",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Por favor, lea estos términos y condiciones cuidadosamente antes de usar FinanzApp.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "1. Aceptación de términos",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Al utilizar nuestra aplicación, usted acepta cumplir con estos términos.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                Text(
                        text = "2. Uso permitido",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Esta app está destinada solo para uso personal. Está prohibido el uso comercial sin autorización.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "3. Privacidad",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Nos comprometemos a proteger sus datos personales. Consulte nuestra Política de Privacidad para más detalles.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "4. Modificaciones",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Nos reservamos el derecho de modificar estos términos en cualquier momento.",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "5. Contacto",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Text(
                        text = "Si tiene alguna pregunta sobre estos términos, contáctenos en soporte@finanzapp.com",
                    style = MaterialTheme.typography.bodyMedium
                )
                }
                Spacer(modifier = Modifier.weight(0.2f))


                Button(
                    onClick = {
                        navController.popBackStack()
                    },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(width = 200.dp, height = 48.dp)
                        .align(Alignment.CenterHorizontally),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                ) {
                    Text(
                        text = "Volver",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}
