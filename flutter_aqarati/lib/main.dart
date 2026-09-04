import 'package:flutter/material.dart';
import 'package:flutter_localizations/flutter_localizations.dart';
import 'package:provider/provider.dart';
import 'providers/property_provider.dart';
import 'theme/app_theme.dart';
import 'screens/home_screen.dart';
import 'screens/favorites_screen.dart';
import 'screens/calculator_screen.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(
    ChangeNotifierProvider(
      create: (_) => PropertyProvider(),
      child: const AqariApp(),
    ),
  );
}

class AqariApp extends StatelessWidget {
  const AqariApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'عقاراتي',
      debugShowCheckedModeBanner: false,
      theme: AppTheme.darkTheme,
      locale: const Locale('ar', 'SA'),
      supportedLocales: const [
        Locale('ar', 'SA'),
        Locale('ar'),
        Locale('en'),
      ],
      localizationsDelegates: const [
        GlobalMaterialLocalizations.delegate,
        GlobalWidgetsLocalizations.delegate,
        GlobalCupertinoLocalizations.delegate,
      ],
      home: const MainNavigationScreen(),
    );
  }
}

class MainNavigationScreen extends StatefulWidget {
  const MainNavigationScreen({super.key});

  @override
  State<MainNavigationScreen> createState() => _MainNavigationScreenState();
}

class _MainNavigationScreenState extends State<MainNavigationScreen> {
  int _currentIndex = 0;

  void _switchToTab(int index) {
    setState(() => _currentIndex = index);
  }

  @override
  Widget build(BuildContext context) {
    final provider = context.watch<PropertyProvider>();
    final favCount = provider.favoriteProperties.length;

    final screens = [
      HomeScreen(onOpenCalculator: () => _switchToTab(2)),
      FavoritesScreen(
        onExploreTap: () => _switchToTab(0),
        onOpenCalculator: () => _switchToTab(2),
      ),
      const CalculatorScreen(),
    ];

    return Scaffold(
      body: screens[_currentIndex],
      bottomNavigationBar: NavigationBar(
        selectedIndex: _currentIndex,
        backgroundColor: const Color(0xFF181F29),
        indicatorColor: AppTheme.goldAccent.withOpacity(0.2),
        onDestinationSelected: _switchToTab,
        destinations: [
          const NavigationDestination(
            icon: Icon(Icons.explore_outlined),
            selectedIcon: Icon(Icons.explore, color: AppTheme.goldAccent),
            label: "تصفح",
          ),
          NavigationDestination(
            icon: Badge(
              isLabelVisible: favCount > 0,
              label: Text("$favCount"),
              backgroundColor: AppTheme.goldAccent,
              textColor: AppTheme.primaryOnColor,
              child: const Icon(Icons.favorite_outline),
            ),
            selectedIcon: Icon(
              Icons.favorite,
              color: favCount > 0 ? AppTheme.favoriteRed : AppTheme.goldAccent,
            ),
            label: "المفضلة",
          ),
          const NavigationDestination(
            icon: Icon(Icons.calculate_outlined),
            selectedIcon: Icon(Icons.calculate, color: AppTheme.goldAccent),
            label: "حاسبة التمويل",
          ),
        ],
      ),
    );
  }
}
