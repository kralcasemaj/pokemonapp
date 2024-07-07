package com.example.pokemonlist

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight

val appFontFamily = FontFamily(
    fonts = listOf(
        Font(resId = R.font.circularstd_book),
        Font(resId = R.font.circularstd_medium, weight = FontWeight.W600),
        Font(resId = R.font.circularstd_black, weight = FontWeight.Bold),
        Font(resId = R.font.circularstd_bold, weight = FontWeight.W900)
    )
)

private val defaultTypography = Typography()
val themeTypography = Typography(
    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = appFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = appFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = appFontFamily),
    titleLarge = defaultTypography.titleLarge.copy(fontFamily = appFontFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = appFontFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = appFontFamily),
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = appFontFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = appFontFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = appFontFamily),
    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = appFontFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = appFontFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = appFontFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = appFontFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = appFontFamily),
    labelLarge = defaultTypography.labelLarge.copy(fontFamily = appFontFamily)
)