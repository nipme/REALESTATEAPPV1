package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// =========================================================================
// Royal Dark Blue Palette (No Yellow - Replaced with Deep Navy & Royal Blue)
// =========================================================================

// Dark Theme Canvas Tokens (Deep Sapphire Navy)
val DeepNavyBg = Color(0xFF0B132B)
val DeepNavySurface = Color(0xFF131E3A)
val DeepNavySurfaceVariant = Color(0xFF1C2C4E)
val DeepNavyNav = Color(0xFF090F22)
val DeepNavyOutline = Color(0xFF2B3F68)

// Deep Blue & Royal Accent Tokens
val RoyalBlueDark = Color(0xFF0F2B48)
val RoyalBluePrimary = Color(0xFF2563EB)      // Vibrant Royal Blue
val RoyalBlueLight = Color(0xFF60A5FA)        // Sky Sapphire for dark mode
val RoyalBlueContainerDark = Color(0xFF1E3A8A)// Deep Navy Blue
val RoyalBlueOnContainerDark = Color(0xFFDBEAFE)

val DeepNavyText = Color(0xFFF1F5F9)
val DeepNavyTextSecondary = Color(0xFF94A3B8)
val DeepNavyTextTertiary = Color(0xFF64748B)
val DeepNavyCard = Color(0xFF131E3A)
val DeepNavyCardBorder = Color(0xFF263959)

// Light Theme Canvas Tokens (Clean Bright with Deep Navy Accents)
val LightBg = Color(0xFFF8FAFC)
val LightSurface = Color(0xFFFFFFFF)
val LightSurfaceVariant = Color(0xFFF1F5F9)
val LightNav = Color(0xFFFFFFFF)
val LightOutline = Color(0xFFCBD5E1)
val LightPrimary = Color(0xFF1E3A8A)          // Deep Royal Navy Blue
val LightOnPrimary = Color(0xFFFFFFFF)
val LightPrimaryContainer = Color(0xFFDBEAFE) // Soft ice blue
val LightOnPrimaryContainer = Color(0xFF1E3A8A)
val LightText = Color(0xFF0F172A)
val LightTextSecondary = Color(0xFF475569)
val LightTextTertiary = Color(0xFF94A3B8)

// Functional Status & Badge Colors
val TagGreen = Color(0xFF10B981)              // Emerald Green for Rent / Available
val TagGreenBg = Color(0xFF064E3B)
val TagGreenBgLight = Color(0xFFD1FAE5)

val TagOwnerBlue = Color(0xFF0284C7)          // Ocean Blue for "من المالك مباشرة"
val TagOwnerBg = Color(0xFF082F49)
val TagOwnerBgLight = Color(0xFFE0F2FE)

val TagOfficeIndigo = Color(0xFF6366F1)       // Indigo for "مكتب عقاري"
val TagOfficeBg = Color(0xFF1E1B4B)
val TagOfficeBgLight = Color(0xFFEEF2FF)

val TagBrokerCyan = Color(0xFF06B6D4)         // Cyan for "وسيط معتمد فال"
val TagBrokerBg = Color(0xFF164E63)
val TagBrokerBgLight = Color(0xFFCFFAFE)

val FavoriteRed = Color(0xFFEF4444)
val FavoriteRedBg = Color(0xFF7F1D1D)
val FavoriteRedBgLight = Color(0xFFFEE2E2)

// Compatibility Aliases mapped to Royal Dark Blue (Strictly NO Yellow)
val ElegantDarkBg = DeepNavyBg
val ElegantDarkSurface = DeepNavySurface
val ElegantDarkSurfaceVariant = DeepNavySurfaceVariant
val ElegantDarkNav = DeepNavyNav
val ElegantDarkOutline = DeepNavyOutline
val ElegantDarkPrimary = RoyalBlueLight
val ElegantDarkOnPrimary = DeepNavyBg
val ElegantDarkPrimaryContainer = RoyalBlueContainerDark
val ElegantDarkOnPrimaryContainer = RoyalBlueOnContainerDark
val ElegantDarkSecondary = Color(0xFF93C5FD)
val ElegantDarkOnSecondary = DeepNavyBg
val ElegantDarkText = DeepNavyText
val ElegantDarkTextSecondary = DeepNavyTextSecondary
val ElegantDarkTextTertiary = DeepNavyTextTertiary
val ElegantDarkCard = DeepNavyCard
val ElegantDarkCardHover = Color(0xFF1E2E4A)

// Replaced yellow/gold with Deep Navy Blue Accents
val GoldAccent = RoyalBlueLight
val GoldLight = RoyalBlueOnContainerDark
val GoldDark = RoyalBlueContainerDark

val AccentTagGreen = TagGreen
val AccentTagGreenBg = TagGreenBg
val EmeraldTag = TagGreen
val EmeraldTagLight = TagGreenBg

val Navy900 = DeepNavyBg
val Navy800 = DeepNavySurface
val Navy700 = DeepNavySurfaceVariant
val Navy600 = DeepNavyOutline

val Slate50 = LightBg
val Slate100 = LightSurfaceVariant
val Slate200 = LightOutline
val Slate400 = DeepNavyTextTertiary
val Slate600 = DeepNavyTextSecondary
val Slate800 = DeepNavyText
val Slate900 = Color(0xFFFFFFFF)
