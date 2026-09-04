import 'package:flutter/material.dart';

class AppTheme {
  static const Color darkBg = Color(0xFF121820);
  static const Color darkSurface = Color(0xFF1B222D);
  static const Color darkSurfaceVariant = Color(0xFF232B38);
  static const Color darkOutline = Color(0xFF2A3444);
  static const Color goldAccent = Color(0xFFD4AF37);
  static const Color primaryOnColor = Color(0xFF121820);
  static const Color textMain = Color(0xFFF1F5F9);
  static const Color textSecondary = Color(0xFF94A3B8);
  static const Color tagGreen = Color(0xFF4ADE80);
  static const Color tagGreenBg = Color(0xFF143823);
  static const Color favoriteRed = Color(0xFFEF4444);

  static ThemeData get darkTheme {
    return ThemeData(
      useMaterial3: true,
      brightness: Brightness.dark,
      scaffoldBackgroundColor: darkBg,
      colorScheme: const ColorScheme.dark(
        primary: goldAccent,
        onPrimary: primaryOnColor,
        surface: darkSurface,
        surfaceContainerHighest: darkSurfaceVariant,
        outline: darkOutline,
        error: favoriteRed,
      ),
      appBarTheme: const AppBarTheme(
        backgroundColor: darkSurface,
        elevation: 0,
        centerTitle: false,
        iconTheme: IconThemeData(color: textMain),
        titleTextStyle: TextStyle(
          color: textMain,
          fontSize: 18,
          fontWeight: FontWeight.bold,
        ),
      ),
      cardTheme: CardTheme(
        color: darkSurface,
        elevation: 1,
        shape: RoundedRectangleBorder(
          borderRadius: BorderRadius.circular(18),
          side: const BorderSide(color: darkOutline, width: 0.6),
        ),
      ),
      inputDecorationTheme: InputDecorationTheme(
        filled: true,
        fillColor: darkSurfaceVariant,
        hintStyle: const TextStyle(color: textSecondary, fontSize: 14),
        border: OutlineInputBorder(
          borderRadius: BorderRadius.circular(14),
          borderSide: const BorderSide(color: darkOutline),
        ),
        enabledBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(14),
          borderSide: const BorderSide(color: darkOutline),
        ),
        focusedBorder: OutlineInputBorder(
          borderRadius: BorderRadius.circular(14),
          borderSide: const BorderSide(color: goldAccent, width: 1.5),
        ),
      ),
    );
  }
}
