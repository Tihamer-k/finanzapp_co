package com.tihcodes.finanzapp.co.presentation.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import finanzapp_co.composeapp.generated.resources.LeagueSpartan_Black
import finanzapp_co.composeapp.generated.resources.LeagueSpartan_Bold
import finanzapp_co.composeapp.generated.resources.LeagueSpartan_ExtraBold
import finanzapp_co.composeapp.generated.resources.LeagueSpartan_ExtraLight
import finanzapp_co.composeapp.generated.resources.LeagueSpartan_Light
import finanzapp_co.composeapp.generated.resources.LeagueSpartan_Medium
import finanzapp_co.composeapp.generated.resources.LeagueSpartan_Regular
import finanzapp_co.composeapp.generated.resources.LeagueSpartan_SemiBold
import finanzapp_co.composeapp.generated.resources.LeagueSpartan_Thin
import finanzapp_co.composeapp.generated.resources.Poppins_Black
import finanzapp_co.composeapp.generated.resources.Poppins_BlackItalic
import finanzapp_co.composeapp.generated.resources.Poppins_Bold
import finanzapp_co.composeapp.generated.resources.Poppins_BoldItalic
import finanzapp_co.composeapp.generated.resources.Poppins_ExtraBold
import finanzapp_co.composeapp.generated.resources.Poppins_ExtraBoldItalic
import finanzapp_co.composeapp.generated.resources.Poppins_ExtraLight
import finanzapp_co.composeapp.generated.resources.Poppins_ExtraLightItalic
import finanzapp_co.composeapp.generated.resources.Poppins_Italic
import finanzapp_co.composeapp.generated.resources.Poppins_Light
import finanzapp_co.composeapp.generated.resources.Poppins_LightItalic
import finanzapp_co.composeapp.generated.resources.Poppins_Medium
import finanzapp_co.composeapp.generated.resources.Poppins_MediumItalic
import finanzapp_co.composeapp.generated.resources.Poppins_Regular
import finanzapp_co.composeapp.generated.resources.Poppins_SemiBold
import finanzapp_co.composeapp.generated.resources.Poppins_SemiBoldItalic
import finanzapp_co.composeapp.generated.resources.Poppins_Thin
import finanzapp_co.composeapp.generated.resources.Poppins_ThinItalic
import finanzapp_co.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.Font


@Composable
fun AppTypography(): Typography {
    val displayFontFamily = FontFamily(
        Font(Res.font.Poppins_Bold, FontWeight.Bold),
        Font(Res.font.Poppins_Regular, FontWeight.Normal),
        Font(Res.font.Poppins_Medium, FontWeight.Medium),
        Font(Res.font.Poppins_Light, FontWeight.Light),
        Font(Res.font.Poppins_Italic, FontWeight.Normal, FontStyle.Italic),
        Font(Res.font.Poppins_SemiBold, FontWeight.SemiBold),
        Font(Res.font.Poppins_Black, FontWeight.Black),
        Font(Res.font.Poppins_ExtraBold, FontWeight.ExtraBold),
        Font(Res.font.Poppins_ExtraLight, FontWeight.ExtraLight),
        Font(Res.font.Poppins_Thin, FontWeight.Thin),
        Font(Res.font.Poppins_LightItalic, FontWeight.Light, FontStyle.Italic),
        Font(Res.font.Poppins_MediumItalic, FontWeight.Medium, FontStyle.Italic),
        Font(Res.font.Poppins_Italic, FontWeight.Normal, FontStyle.Italic),
        Font(Res.font.Poppins_ExtraBoldItalic, FontWeight.ExtraBold, FontStyle.Italic),
        Font(Res.font.Poppins_ExtraLightItalic, FontWeight.ExtraLight, FontStyle.Italic),
        Font(Res.font.Poppins_ThinItalic, FontWeight.Thin, FontStyle.Italic),
        Font(Res.font.Poppins_BlackItalic, FontWeight.Black, FontStyle.Italic),
        Font(Res.font.Poppins_SemiBoldItalic, FontWeight.SemiBold, FontStyle.Italic),
        Font(Res.font.Poppins_BoldItalic, FontWeight.Bold, FontStyle.Italic),

        )

    val bodyFontFamily = FontFamily(
        Font(Res.font.LeagueSpartan_Black, FontWeight.Black),
        Font(Res.font.LeagueSpartan_Bold, FontWeight.Bold),
        Font(Res.font.LeagueSpartan_ExtraBold, FontWeight.ExtraBold),
        Font(Res.font.LeagueSpartan_ExtraLight, FontWeight.ExtraLight),
        Font(Res.font.LeagueSpartan_Light, FontWeight.Light),
        Font(Res.font.LeagueSpartan_Medium, FontWeight.Medium),
        Font(Res.font.LeagueSpartan_Regular, FontWeight.Normal),
        Font(Res.font.LeagueSpartan_SemiBold, FontWeight.SemiBold),
        Font(Res.font.LeagueSpartan_Thin, FontWeight.Thin)
    )

    return Typography(
        displayLarge = TextStyle(fontFamily = displayFontFamily, fontWeight = FontWeight.Bold, fontSize = 28.sp),
        displayMedium = TextStyle(fontFamily = displayFontFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp),
        displaySmall = TextStyle(fontFamily = displayFontFamily, fontWeight = FontWeight.Medium, fontSize = 16.sp),
        headlineLarge = TextStyle(fontFamily = displayFontFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp),
        headlineMedium = TextStyle(fontFamily = displayFontFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp),
        headlineSmall = TextStyle(fontFamily = displayFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp),
        titleLarge = TextStyle(fontFamily = displayFontFamily, fontWeight = FontWeight.Bold, fontSize = 24.sp),
        titleMedium = TextStyle(fontFamily = displayFontFamily, fontWeight = FontWeight.Bold, fontSize = 20.sp),
        titleSmall = TextStyle(fontFamily = displayFontFamily, fontWeight = FontWeight.Bold, fontSize = 16.sp),
        bodyLarge = TextStyle(fontFamily = bodyFontFamily,  fontSize = 24.sp, fontWeight = FontWeight.Normal),
        bodyMedium = TextStyle(fontFamily = bodyFontFamily, fontSize = 16.sp, fontWeight = FontWeight.Normal),
        bodySmall = TextStyle(fontFamily = bodyFontFamily,  fontSize = 14.sp, fontWeight = FontWeight.Medium),
        labelLarge = TextStyle(fontFamily = bodyFontFamily, fontSize = 24.sp, fontWeight = FontWeight.Black),
        labelMedium = TextStyle(fontFamily = bodyFontFamily, fontSize = 16.sp, fontWeight = FontWeight.Bold),
        labelSmall = TextStyle(fontFamily = bodyFontFamily, fontSize = 14.sp, fontWeight = FontWeight.SemiBold),

    )
}
