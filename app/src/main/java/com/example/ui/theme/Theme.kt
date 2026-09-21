package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
  primary = AusGold,
  onPrimary = Color(0xFF070D1E),
  primaryContainer = AusCardDark,
  onPrimaryContainer = AusGoldLight,
  secondary = AusGreen,
  onSecondary = Color(0xFF070D1E),
  secondaryContainer = Color(0xFF003816),
  onSecondaryContainer = AusGreenLight,
  tertiary = AusAccentCyan,
  onTertiary = Color.White,
  background = AusNavyDark,
  onBackground = AusDarkTextPrimary,
  surface = AusSurfaceDark,
  onSurface = AusDarkTextPrimary,
  surfaceVariant = AusCardDark,
  onSurfaceVariant = AusDarkTextSecondary,
  outline = AusCardStrokeDark,
  error = AusRed,
  onError = Color.White
)

private val LightColorScheme = lightColorScheme(
  primary = AusPrimaryBlue,
  onPrimary = Color.White,
  primaryContainer = Color(0xFFD6E4FF),
  onPrimaryContainer = Color(0xFF001B3F),
  secondary = AusGreen,
  onSecondary = Color.White,
  secondaryContainer = Color(0xFFD1FADF),
  onSecondaryContainer = Color(0xFF054F24),
  tertiary = AusGold,
  onTertiary = Color.Black,
  background = AusLightBg,
  onBackground = AusTextPrimaryLight,
  surface = AusLightSurface,
  onSurface = AusTextPrimaryLight,
  surfaceVariant = AusLightCard,
  onSurfaceVariant = AusTextSecondaryLight,
  outline = AusLightCardStroke,
  error = AusRed,
  onError = Color.White
)

@Composable
fun AustraliaMarketTheme(
  darkTheme: Boolean = true, // Default to dark for premium financial trading look
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

// Backward compatibility wrapper for existing tests
@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = false,
  content: @Composable () -> Unit
) {
  AustraliaMarketTheme(darkTheme = darkTheme, content = content)
}
